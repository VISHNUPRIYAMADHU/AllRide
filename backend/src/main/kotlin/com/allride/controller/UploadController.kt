package com.allride.controller

import com.allride.publisher.EventPublisher
import kotlinx.coroutines.channels.Channel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api")
class UploadController(private val fileEventChannel: Channel<String>) {

    private val uploadDir = "uploads"

    init {
        File(uploadDir).mkdirs()
    }

    @PostMapping("/upload")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty || !file.originalFilename!!.endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Invalid file type")
        }

        val filePath = Path.of(uploadDir, file.originalFilename!!)
        Files.copy(file.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        // Log before publishing event
        println("Publishing file event for path: $filePath")

        // Publish the event
        fileEventChannel.trySend(filePath.toString())
        // Log after publishing event
    if (result.isSuccess) {
        println("Event published successfully for file: $filePath")
    } else {
        println("Failed to publish event for file: $filePath, reason: ${result.isFailure}")
    }

        return ResponseEntity.ok("File uploaded successfully. Processing started.")
    }
}
