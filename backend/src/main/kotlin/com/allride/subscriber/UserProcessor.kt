package com.allride.subscriber

import com.allride.model.User
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import org.springframework.stereotype.Component
import java.io.FileReader

@Component
class UserProcessor(fileEventChannel: Channel<String>) {

    private val userStore = mutableListOf<User>() // in-memory storage

    init {
        CoroutineScope(Dispatchers.IO).launch {
            for (filePath in fileEventChannel) {
             println("Received event for file: $filePath")   // <-- log event reception

                processFile(filePath)
            }
        }
    }

    private fun processFile(path: String) {
        println("Started processing: $path")
        try {
            val reader = CSVReaderBuilder(FileReader(path)).withSkipLines(1).build()
            val records = reader.readAll()
                        println("Read ${records.size} records from file: $path")  // <-- log how many records

            for (row in records) {
                try {
                    val user = User(row[0], row[1], row[2], row[3])
                    userStore.add(user)
                    println("Stored user: $user")
                } catch (e: Exception) {
                    println("Invalid row: ${row.joinToString()} -- ${e.message}")
                }
            }
            println("Finished processing: $path")
        } catch (e: Exception) {
            println("Failed to read file: $path -- ${e.message}")
        }
    }
}
