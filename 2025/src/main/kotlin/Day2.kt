@file:OptIn(ExperimentalCoroutinesApi::class)

package io.overclockmp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.mapNotNull
import org.jetbrains.annotations.VisibleForTesting

private val DUPLICATED_N_TIMES_REGEX = "^(\\d+)\\1+$".toRegex()
private val DUPLICATED_TWICE_REGEX = "^(\\d+)\\1$".toRegex()

@VisibleForTesting
internal data class StringRange(
    val first: String,
    val last: String
) : Iterable<String>, ClosedRange<String>, OpenEndRange<String> {

    private val firstValue: ULong by lazy {
        first.toULongOrNull() ?: throw IllegalArgumentException("Invalid range start: $first")
    }
    private val lastValue: ULong by lazy {
        last.toULongOrNull() ?: throw IllegalArgumentException("Invalid range end: $last")
    }
    override val start: String get() = first

    override val endInclusive: String get() = last

    override val endExclusive: String
        get() {
            val longValue = last.toULong()
            if (longValue == ULong.MAX_VALUE) error("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.")
            return (longValue + 1u).toString()
        }

    override fun contains(value: String): Boolean {
        val numValue = value.toULongOrNull() ?: return false
        return numValue in firstValue..lastValue
    }

    override fun isEmpty(): Boolean = firstValue > lastValue

    override fun iterator(): Iterator<String> = StringProgressionIterator(firstValue, lastValue)
}

private class StringProgressionIterator(first: ULong, last: ULong) : Iterator<String> {

    private var current = first
    private val lastValue = last
    private var hasNext: Boolean = first <= last

    override fun hasNext(): Boolean = hasNext

    override fun next(): String {
        if (!hasNext) throw NoSuchElementException()

        val value = current.toString()
        if (current == lastValue) {
            hasNext = false
        } else {
            current++
        }
        return value
    }
}


private fun String.toRangeList(): List<StringRange> {
    return this.split(',').map {
        it.trim().toRange()
    }
}

private fun String.toRange(): StringRange {
    val rangeValues = this.split('-')
    require(rangeValues.size == 2) { "Invalid range format: $this" }
    return StringRange(rangeValues.first().trim(), rangeValues.last().trim())
}

private fun String.isValid(supportNDuplicates: Boolean) =
    if (supportNDuplicates) {
        !containsNumberDuplicatedNTimes()
    } else {
        !containsNumberDuplicatedTwice()
    }

private fun String.containsNumberDuplicatedNTimes(): Boolean = this.matches(DUPLICATED_N_TIMES_REGEX)

private fun String.containsNumberDuplicatedTwice(): Boolean = this.matches(DUPLICATED_TWICE_REGEX)

object Day2 {
    context(log: Log)
    suspend fun addAllInvalidIds(idRanges: String, supportNDuplicates: Boolean = false): ULong {
        return idRanges.toRangeList().asFlow()
            .flatMapMerge { range ->
                range.asFlow()
                    .mapNotNull { id ->
                        if (id.isValid(supportNDuplicates)) null else id.toULongOrNull()
                    }.flowOn(Dispatchers.Default) // check every range concurrently
            }.log("Invalid Id: ")
            .fold(0u) { acc, id ->
                acc + id
            }
    }
}
