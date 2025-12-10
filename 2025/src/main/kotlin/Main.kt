package io.overclockmp

import io.overclockmp.Day2.addAllInvalidIds
import io.overclockmp.Day3.findHighestOutputJoltage


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
}


suspend fun main() {
    day1()
    day2()
    day3()
}