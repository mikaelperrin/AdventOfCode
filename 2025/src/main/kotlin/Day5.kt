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

        // read and merge all the ranges that can be merged so that there is no intersection to avoid duplicates
        val mergedRanges = sortedSetOf<LongRange>(Comparator { r1, r2 -> r1.first.compareTo(r2.first) })
        for(stringRange in ranges) {
            val parts = stringRange.split('-')
            val range = LongRange(parts[0].toLong(), parts[1].toLong())
            mergedRanges.addAndMerge(range)
        }

        log.d("MergedRanges: $this")

        // count each range by diffing
        return mergedRanges.sumOf { it.last - it.first + 1 }
    }

    context(log: Log)
    private fun SortedSet<LongRange>.addAndMerge(range: LongRange) {

        var inserted = false

        //insert new range
        for(recordedRange in this) {
            if(recordedRange.intersectsWith(range)) {
                val newRange = recordedRange.mergeWith(range)
                this.remove(recordedRange)
                this.add(newRange)
                inserted = true
                break
            }
        }

        if(!inserted) {
            this.add(range)
            return
        }

        val list = this.toMutableList()
        val mergedList = mutableListOf<LongRange>()

        var i = 0
        while(i < list.size) {
            if(i+1 < list.size && list[i].intersectsWith(list[i+1])) {
                mergedList.add(list[i].mergeWith(list[i+1]))
                i += 2
            } else {
                mergedList.add(list[i])
                ++i
            }
        }
        this.clear()
        this.addAll(mergedList)
    }

    context(log: Log)
    private fun LongRange.intersectsWith(other: LongRange) : Boolean {
        if(this.first > other.last || this.last < other.first) {
            log.d("Intersection: there is no intersection between $this and $other")
            return false
        }
        if(this.first >= other.first && this.last <= other.last) {
            log.d("Intersection: $$this is contained in $other")
            return true
        }

        if(this.first <= other.first && this.last >= other.last) {
            log.d("Intersection: $other is contained in $this")
            return true
        }

        return if(this.last > other.first && this.last < other.last) {
            log.d("Intersection: end of $this overlaps with beginning of $other")
            true
        } else {
            log.d("Intersection: beginning of $this overlaps with end of $other")
            true
        }
    }

    // assumes ranges collide already
    private fun LongRange.mergeWith(other: LongRange): LongRange {
        val newStart = if(this.first < other.first) {
            this.first
        } else {
            other.first
        }
        val newEnd = if(this.last > other.last) {
            this.last
        } else {
            other.last
        }
        return LongRange(newStart, newEnd)
    }
}