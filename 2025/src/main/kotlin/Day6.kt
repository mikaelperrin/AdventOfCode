package io.overclockmp

object Day6 {
    fun solveProblemsPart1(problemLines: List<String>): Long {

        val symbols = problemLines.last()
            .trim()
            .split("\\s+".toRegex())
            .map { it[0] }

        val resultTable = LongArray(symbols.size)
        val initialized = BooleanArray(symbols.size) { false }

        for (i in 0 until problemLines.size-1) {
            val parts = problemLines[i].trim().split("\\s+".toRegex())

            parts.forEachIndexed { index, number ->
                val numberValue = number.toLong()
                resultTable[index] = if (!initialized[index]) {
                    numberValue.also {
                        initialized[index] = true
                    }
                } else {
                    when (val symbol = symbols[index]) {
                        '+' -> {
                            resultTable[index] + numberValue
                        }
                        '*' -> {
                            resultTable[index] * numberValue
                        }
                        else -> throw UnsupportedOperationException("What is this symbol now? $symbol")
                    }
                }
            }
        }


        return resultTable.sum()
    }

    fun solveProblemsPart2(problemLines: List<String>): Long {
        val symbols = problemLines.last()
            .trim()
            .split("\\s+".toRegex())
            .map { it[0] }


        val lineSize = problemLines.first().length
        val results = mutableListOf<Long>()
        val numbers = mutableListOf<Long>()
        var symbolIndex = symbols.lastIndex

        for(column in lineSize-1 downTo 0) {
            var number = 0L
            for (lineIndex in 0..<problemLines.lastIndex) {
                val digitOrEmpty = problemLines[lineIndex][column]
                if (digitOrEmpty.isDigit()) {
                    number = number * 10 + digitOrEmpty.digitToInt() //shift left, then insert
                }
            }

            if (number != 0L) {
                numbers.add(number)
            }

            if (number == 0L || column == 0) { //empty column or last column, number list is over, time to calculate
                val result = when (val symbol = symbols[symbolIndex]) {
                    '+' -> {
                        numbers.sum()
                    }
                    '*' -> {
                        numbers.fold(1L) { acc, n -> acc * n }
                    }
                    else -> throw UnsupportedOperationException("What is this symbol now? $symbol")
                }
                results.add(result)
                numbers.clear()
                --symbolIndex
            }
        }

        return results.sum()
    }
}