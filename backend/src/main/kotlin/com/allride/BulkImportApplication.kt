package com.allride

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BulkImportApplication

fun main(args: Array<String>) {
    runApplication<BulkImportApplication>(*args)
}
