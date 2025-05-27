package com.allride.subscriber

import com.allride.event.FileUploadedEvent
import com.allride.model.User
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import org.springframework.stereotype.Component
import java.io.FileReader

@Component
class UserProcessor(fileEventChannel: Channel<FileUploadedEvent>) {

    private val userStore = mutableListOf<User>() // in-memory storage

    init {
        CoroutineScope(Dispatchers.IO).launch {
            for (event in fileEventChannel) {
                println("📨 Received event for file: ${event.filePath} at ${event.timestamp}")

                processFile(event.filePath)
            }
        }
    }

   private fun processFile(path: String) {
    println("🚀 Started processing: $path")
    val errors = mutableListOf<String>()
    try {
        val reader = CSVReaderBuilder(FileReader(path)).withSkipLines(1).build()
        val records = reader.readAll()

        println("✅ Read ${records.size} records from file: $path")

        for ((index, row) in records.withIndex()) {
            try {
                if (row.size < 4) {
                    errors.add("Line ${index + 2}: Expected 4 columns but found ${row.size}")
                    continue
                }

                val id = row[0].trim()
                val firstName = row[1].trim()
                val lastName = row[2].trim()
                val email = row[3].trim()

                if (!email.contains("@")) {
                    errors.add("Line ${index + 2}: Invalid email '$email'")
                    continue
                }

                val user = User(id, firstName, lastName, email)
                userStore.add(user)
                println("👤 Stored user: $user")

            } catch (e: Exception) {
                errors.add("Line ${index + 2}: Exception processing row - ${e.message}")
            }
        }

        if (errors.isNotEmpty()) {
            println("⚠️ Found ${errors.size} errors processing file:")
            errors.forEach { println(it) }
        }

        // Print entire userStore after processing
        println("📦 Current user store:")
        userStore.forEach { println(it) }

        println("🎉 Finished processing: $path")
    } catch (e: Exception) {
        println("💥 Failed to read file: $path -- ${e.message}")
    }
}

}
