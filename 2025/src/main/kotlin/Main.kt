package io.overclockmp


suspend fun main() {
    val log = Log()

    log.i("Day 1 - Secret Entrance")
    log.i("Part 1")
    val lines = getFileContent("day1_input")
    val passwordPart1 = Day1.findPassword(lines.toTypedArray(), use0x434C49434BMethod = false)
    log.i("Password: $passwordPart1")
    log.i("Part 2 - with 0x434C49434B method")
    val passwordPart2 = Day1.findPassword(lines.toTypedArray())
    log.i("Password: $passwordPart2")

}