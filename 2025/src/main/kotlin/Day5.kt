@file:OptIn(ExperimentalCoroutinesApi::class)

package io.overclockmp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.SortedSet

object Day5 {


    context(log: Log)
    suspend fun findNumberOfFreshProductsFromInventory(ranges: List<String>, productIDs: List<String>): Int {
        val longRanges: List<LongRange> = ranges.map {
            val parts = it.split('-')
            LongRange(parts[0].toLong(), parts[1].toLong())
        }
        return productIDs.asFlow()
            .map { it.toLong() }
            .flatMapMerge { productID ->
                flow {
                    longRanges.find { range -> productID in range }?.let {
                        emit(productID)
                    }
                }.flowOn(Dispatchers.Default)
            }.log()
            .count()
    }

    context(log: Log)
    fun findAllFreshProductIDs(ranges: List<String>): Long {

        val parsedRanges = ranges.map { stringRange ->
            val parts = stringRange.split('-')
            LongRange(parts[0].toLong(), parts[1].toLong())
        }.sortedBy { it.first }

        val mergedRanges = mutableListOf<LongRange>()
        var current = parsedRanges[0]

        for (i in 1 until parsedRanges.size) {
            val next = parsedRanges[i]
            log.d("Current Range: $current | Next Range: $next")
            if (current.last >= next.first - 1) {
                log.d("Found collision between $current and $next")
                current = LongRange(current.first, maxOf(current.last, next.last))
            } else {
                mergedRanges.add(current)
                current = next
            }
            log.d("Current Merged Ranges: $mergedRanges")
        }
        mergedRanges.add(current)
        log.d("Merged Ranges: $mergedRanges")

        return mergedRanges.sumOf { it.last - it.first + 1 }
    }
}