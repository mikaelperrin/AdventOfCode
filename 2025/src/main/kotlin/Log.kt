package io.overclockmp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log


private const val TAG = "AdventCalendar2025"
private val LOG_LEVEL = LogLevel.INFO

private const val RED = "\u001b[31m"
private const val RESET = "\u001b[0m"

typealias ILogger = (String) -> Unit

class Log {
    val d = baseLogger()
        .withApplicationTag(TAG)
        .withTime()
        .withLogLevelFormat(LogLevel.DEBUG)
        .autoTag()
        .onlyIf(LOG_LEVEL <= LogLevel.DEBUG)

    val i = baseLogger()
        .withApplicationTag(TAG)
        .withTime()
        .withLogLevelFormat(LogLevel.INFO)

    val e = baseLogger()
        .withApplicationTag(TAG)
        .withLogLevelFormat(LogLevel.ERROR)
}

enum class LogLevel(val levelTag: String) {
    VERBOSE("V/"),
    DEBUG("D/"),
    INFO("I/"),
    WARNING("W/"),
    ERROR("E/")
}

private fun baseLogger(): ILogger = { message ->
    println(message)
}

private fun ILogger.withLogLevelFormat(logLevel: LogLevel): ILogger = { message ->
    val formattedMessage = buildString {
        if (logLevel == LogLevel.ERROR) {
            append(RED)
        }
        append("${logLevel.levelTag}$message")
        if (logLevel == LogLevel.ERROR) {
            append(RESET)
        }
    }
    this(formattedMessage)
}

private fun ILogger.withApplicationTag(tag: String): ILogger = { message ->
    this("[$tag] $message")
}

private fun ILogger.autoTag(): ILogger = { message ->
    val stackTraceElement = Thread.currentThread().stackTrace[3]
    this("${stackTraceElement.fileName?.substringBefore('.')}(${stackTraceElement.lineNumber}) $message")
}

private fun ILogger.withTime(
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
): ILogger = { message ->
    val timestamp = LocalDateTime.now().format(formatter)
    this(" $timestamp  $message")
}

private fun ILogger.onlyIf(condition: Boolean): ILogger = { message ->
    if (condition) this(message)
}

context(log: Log)
fun <T> Flow<T>.log(
    prefix: String = "",
    suffix: String = "",
) = this.onEach {
    log.d(buildString {
        append(prefix)
        append(" $it ")
        append(suffix)
    })
}