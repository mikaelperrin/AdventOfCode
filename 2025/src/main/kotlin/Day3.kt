@file:OptIn(ExperimentalCoroutinesApi::class)

package io.overclockmp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map

object Day3 {

    context(log: Log)
    suspend fun findHighestOutputJoltage(batteryBanks: List<String>, batteryCount: Int = 2): Long {
        return batteryBanks.asFlow()
            .flatMapMerge { batteryBank ->
                flowOf(batteryBank)
                    .map {
                        findMaxJoltageWithinBatteryBank(it, batteryCount)
                    }.log("$batteryBank -> ")
                    .flowOn(Dispatchers.Default) // find max Joltage for each bank concurrently

            }.fold(0L) { acc, highestJoltage ->
                acc + highestJoltage
            }
    }

    private fun findMaxJoltageWithinBatteryBank(batteryBank: String, batteryCount: Int): Long {
        val max = IntArray(batteryCount)
        val bankLength = batteryBank.length
        val thresholdForFullNumberInsertion = bankLength - batteryCount

        batteryBank.forEachIndexed { index, battery ->
            val joltage = battery.digitToInt()

            for (maxIndex in 0 until batteryCount) {
                val lastIndexWhereThereIsEnoughSpaceToPutRemainingValues = thresholdForFullNumberInsertion + maxIndex
                if (joltage > max[maxIndex] && index <= lastIndexWhereThereIsEnoughSpaceToPutRemainingValues) {
                    max[maxIndex] = joltage
                    max.fill(0, maxIndex + 1, batteryCount)
                    break
                }
            }
        }
        return max.toLong()
    }
}

private fun IntArray.toLong(): Long {
    var result = 0L
    for (digit in this) {
        result = result * 10 + digit //shift left and insert
    }
    return result
}