package io.overclockmp

import io.overclockmp.Day2.addAllInvalidIds


suspend fun day1() {
    val log = Log()

    log.i("Day 1 - Secret Entrance")
    log.i("====Part 1====")
    val lines = getFileContent("day1_input")
    val passwordPart1 = Day1.findPassword(lines.toTypedArray(), use0x434C49434BMethod = false)
    log.i("\tPassword: $passwordPart1\n")
    log.i("====Part 2 - with 0x434C49434B method====")
    val passwordPart2 = Day1.findPassword(lines.toTypedArray())
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
    log.i("\tResult = $result\n")
    log.i("====Part 2====")
    val result2 = with(log) {
        addAllInvalidIds(input, supportNDuplicates = true)
    }
    log.i("\tResult = $result2\n")
}


suspend fun main() {
    day1()
    day2()
}