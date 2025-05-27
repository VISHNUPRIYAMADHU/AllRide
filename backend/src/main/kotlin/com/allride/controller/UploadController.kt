package com.allride.controller
import com.allride.model.User

import com.allride.event.FileUploadedEvent
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
class UploadController(private val fileEventChannel: Channel<FileUploadedEvent>) {

    private val uploadDir = "uploads"

    init {
        File(uploadDir).mkdirs()
    }

    @PostMapping("/upload")
fun handleFileUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
    if (file.isEmpty) {
        return ResponseEntity.badRequest().body("File is empty")
    }

    if (!file.originalFilename!!.endsWith(".csv", ignoreCase = true)) {
        return ResponseEntity.badRequest().body("Invalid file type, only CSV allowed")
    }

    val lines: List<String>
    try {
        lines = file.inputStream.bufferedReader().readLines()
        if (lines.isEmpty()) {
            return ResponseEntity.badRequest().body("CSV file is empty")
        }
    } catch (ex: Exception) {
        return ResponseEntity.badRequest().body("Failed to read file: ${ex.message}")
    }

    // Header validation
    val headers = lines[0].split(",").map { it.trim().lowercase() }
    val requiredHeaders = listOf("id", "firstname", "lastname", "email")
    val missingHeaders = requiredHeaders.filterNot { it in headers }

    if (missingHeaders.isNotEmpty()) {
        return ResponseEntity.badRequest()
            .body("CSV missing required headers: ${missingHeaders.joinToString(", ")}")
    }

    // Row validation using User model
    val errors = mutableListOf<String>()
    lines.drop(1).withIndex().forEach { (index, line) ->
        val parts = line.split(",").map { it.trim() }
        if (parts.size != 4) {
            errors.add("Line ${index + 2}: Expected 4 fields, got ${parts.size}")
            return@forEach
        }

        try {
            val user = User(
                id = parts[0],
                firstName = parts[1],
                lastName = parts[2],
                email = parts[3]
            )
            // Optional: further validation, like email format
            if (!user.email.contains("@")) {
                errors.add("Line ${index + 2}: Invalid email '${user.email}'")
            }
        } catch (ex: Exception) {
            errors.add("Line ${index + 2}: Failed to parse user - ${ex.message}")
        }
    }

    if (errors.isNotEmpty()) {
        return ResponseEntity.badRequest()
            .body("Invalid data in CSV:\n${errors.joinToString("\n")}")
    }

    // Save and publish event
    val filePath = Path.of(uploadDir, file.originalFilename!!)
    try {
        Files.copy(file.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
    } catch (ex: Exception) {
        return ResponseEntity.internalServerError()
            .body("Failed to save file: ${ex.message}")
    }

    val result = fileEventChannel.trySend(FileUploadedEvent(filePath.toString()))
    if (!result.isSuccess) {
        return ResponseEntity.internalServerError()
            .body("Failed to publish file event: ${result.exceptionOrNull()?.message ?: "unknown error"}")
    }

    return ResponseEntity.ok("File uploaded successfully, processing started")
}



}
