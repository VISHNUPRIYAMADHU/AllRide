package com.allride.event

data class FileUploadedEvent(
    val filePath: String,
    val timestamp: Long = System.currentTimeMillis()
)
