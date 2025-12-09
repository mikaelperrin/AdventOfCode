package io.overclockmp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream


suspend fun getFileContent(fileName: String): List<String> = withContext(Dispatchers.IO) {
    val inputStream: InputStream? = object {}.javaClass.getResourceAsStream("/$fileName")
    inputStream?.bufferedReader()?.use { it.readLines() } ?: emptyList()
}