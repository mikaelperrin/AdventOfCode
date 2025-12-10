package io.overclockmp

import java.awt.Point
import kotlin.collections.indices

object Day4 {

    private const val PAPER_ROLL = '@'

    context(log: Log)
    fun findNumberOfAccessiblePaper(lines: List<String>, keepGoing: Boolean = false): Int {
        val paperRollMap = lines.map { row ->
            row.toCharArray()
        }.toTypedArray()

        val coordinatesOfRemovedPaperRoll = HashSet<Point>()

        var count = 0

        var localCount: Int

        do {
            localCount = 0
            for (i in paperRollMap.indices) {
                for (j in paperRollMap[i].indices) {
                    if(paperRollMap[i][j] == PAPER_ROLL && paperRollMap.hasPaperRollAccessibleAt(i, j)) {
                        coordinatesOfRemovedPaperRoll.add(Point(i, j))
                        ++localCount
                    }
                }
            }

            coordinatesOfRemovedPaperRoll.forEach {
                paperRollMap[it.x][it.y] = 'x'
            }
            log.d("Removed paper rolls")
            paperRollMap.log()

            coordinatesOfRemovedPaperRoll.forEach {
                paperRollMap[it.x][it.y] = '.'
            }
            coordinatesOfRemovedPaperRoll.clear()

            log.d("New roll map:")
            paperRollMap.log()

            count += localCount
        } while (keepGoing && localCount > 0)


        return count
    }

    fun Array<CharArray>.hasPaperRollAccessibleAt(i: Int, j: Int, radius: Int = 1) : Boolean {
        var count = 0
        for (x in i-radius..i+radius) {
            if(x < 0 || x >= this.size) {
                continue
            }
            for (y in j-radius..j+radius) {
                if(x == i && y == j) {
                    continue
                }
                if(y < 0 || y >= this[x].size) {
                    continue
                }
                if(this[x][y] == PAPER_ROLL) {
                    ++count
                }
            }
        }
        return count < 4
    }

    context(log: Log)
    fun Array<CharArray>.log() {
        for (i in this.indices) {
            log.d(this[i].joinToString(""))
        }
    }
}

