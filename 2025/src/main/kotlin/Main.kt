package io.overclockmp

import io.overclockmp.Day2.addAllInvalidIds
import io.overclockmp.Day3.findHighestOutputJoltage
import io.overclockmp.Day4.findNumberOfAccessiblePaper
import io.overclockmp.Day5.findAllFreshProductIDs
import io.overclockmp.Day5.findNumberOfFreshProductsFromInventory
import io.overclockmp.Day6.solveProblemsPart1
import io.overclockmp.Day6.solveProblemsPart2


suspend fun day1() {
    val log = Log()

    log.i("Day 1 - Secret Entrance")
    log.i("====Part 1====")
    val lines = getFileContent("day1_input")
    val passwordPart1 = Day1.findPassword(lines, use0x434C49434BMethod = false)
    log.i("\tPassword: $passwordPart1\n")
    log.i("====Part 2 - with 0x434C49434B method====")
    val passwordPart2 = Day1.findPassword(lines)
    log.i("\tPassword: $passwordPart2\n")
    println()
}

suspend fun day2() {
    val log = Log()

    log.i("Day 2 - Gift Shop")
    log.i("====Part 1====")
    val input = getFileContent("day2_input").first()
    val result = with(log) {
        addAllInvalidIds(input)
    }
    log.i("\tSum of Invalid Ids = $result\n")
    log.i("====Part 2====")
    val result2 = with(log) {
        addAllInvalidIds(input, supportNDuplicates = true)
    }
    log.i("\tSum of Invalid Ids = $result2\n")
    println()
}

suspend fun day3() {
    val log = Log()

    log.i("Day 3 - Lobby")
    log.i("====Part 1====")
    val lines = getFileContent("day3_input")
    val highestJoltage = with(log) {
         findHighestOutputJoltage(lines)
    }
    log.i("\tHighest Joltage = $highestJoltage")
    log.i("====Part 2====")
    val highestJoltage2 = with(log) {
        findHighestOutputJoltage(lines, batteryCount = 12)
    }
    log.i("\tHighest Joltage = $highestJoltage2")
    println()
}

suspend fun day4() {
    val log = Log()
    log.i("Day 4 - Printing Department")
    log.i("====Part 1====")
    val lines = getFileContent("day4_input")
    val actualAccessiblePaper = with(log) {
        findNumberOfAccessiblePaper(lines)
    }
    log.i("\tNumber of accessible paper = $actualAccessiblePaper")
    log.i("====Part 2====")
    val actualAccessiblePaper2 = with(log) {
        findNumberOfAccessiblePaper(lines, keepGoing = true)
    }
    log.i("\tNumber of accessible paper with multiple iteration = $actualAccessiblePaper2")
    println()
}

suspend fun day5() {
    val log = Log()
    log.i("Day 5 - Cafeteria")
    log.i("====Part 1====")
    val lines = getFileContent("day5_input")
    val separatorLineIndex = lines.indexOfFirst{ it.isEmpty() }
    val ranges = lines.take(separatorLineIndex)
    val productIDs = lines.drop(separatorLineIndex + 1)
    val numberOfFreshProductsFromInventory = with(log) {
        findNumberOfFreshProductsFromInventory(ranges, productIDs)
    }
    log.i("Number of Fresh Products found in inventory: $numberOfFreshProductsFromInventory")
    log.i("====Part 2====")
    val allFreshProductIDSCount = with(log) {
        findAllFreshProductIDs(ranges)
    }
    log.i("Total number of Fresh Products:  $allFreshProductIDSCount")
}
suspend fun day6() {
    val log = Log()
    log.i("Day 6 - Trash Compactor")
    log.i("====Part 1====")
    val lines = getFileContent("day6_input")
    val mathResult = solveProblemsPart1(lines)
    log.i("Operation by Line: $mathResult")
    log.i("====Part 2====")
    val mathResult2 = solveProblemsPart2(lines)
    log.i("Cephalopod Math Result: $mathResult2")
}

suspend fun main() {
    day1()
    day2()
    day3()
    day4()
    day5()
    day6()
}
