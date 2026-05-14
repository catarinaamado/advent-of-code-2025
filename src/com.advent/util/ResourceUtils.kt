package com.advent.util

class ResourceReaderUtil {
    companion object Resource {
        fun readResourceLines(resourceName: String): List<String> {
            return this::class.java.classLoader
                .getResourceAsStream(resourceName)
                ?.bufferedReader(Charsets.UTF_8)
                ?.use { it.readLines() }
                ?: error("Resource $resourceName not found")
        }

        fun readResourceText(resourceName: String): String {
            return this::class.java.classLoader
                .getResourceAsStream(resourceName)
                ?.bufferedReader(Charsets.UTF_8)
                ?.use { it.readText() }
                ?: error("Resource $resourceName not found")
        }
    }
}