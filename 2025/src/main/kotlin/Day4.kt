@file:OptIn(ExperimentalCoroutinesApi::class)

package io.overclockmp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import java.awt.Point
import kotlin.collections.flatten
import kotlin.collections.indices
import kotlin.math.max

object Day4 {

    private const val PAPER_ROLL = '@'

    context(log: Log)
    fun findNumberOfAccessiblePaper(lines: List<String>, keepGoing: Boolean = false): Int {
        val paperRollMap = lines.map { row ->
            row.toCharArray()
        }.toTypedArray()

        return if (!keepGoing) { //part 1
            solution1(paperRollMap, false)
        } else { //part 2
            solution2(paperRollMap) //faster
        }
    }

    private fun solution1(
        paperRollMap: Array<CharArray>,
        keepGoing: Boolean
    ): Int {
        var count = 0
        do {
            val accessiblePoints = findAllAccessiblePoints(paperRollMap)
            count += accessiblePoints.size

            // remove accessible rolls
            accessiblePoints.forEach {
                paperRollMap[it.x][it.y] = '.'
            }

        } while (keepGoing && accessiblePoints.isNotEmpty())
        return count
    }

    private fun solution2(paperRollMap: Array<CharArray>): Int {
        val toCheck = ArrayDeque<Point>()

        for (i in paperRollMap.indices) {
            for (j in paperRollMap[i].indices) {
                if (paperRollMap[i][j] == PAPER_ROLL) {
                    toCheck.add(Point(i, j))
                }
            }
        }

        var count = 0
        while (toCheck.isNotEmpty()) {
            val point = toCheck.removeFirst()

            if (paperRollMap[point.x][point.y] != PAPER_ROLL) continue

            if (paperRollMap.nbPaperRollAround(point) < 4) {
                paperRollMap[point.x][point.y] = '.'
                count++

                // Only re-check neighbors of removed paper roll
                for (dx in -1..1) {
                    for (dy in -1..1) {
                        val nx = point.x + dx
                        val ny = point.y + dy
                        if (nx in paperRollMap.indices &&
                            ny in paperRollMap[0].indices &&
                            paperRollMap[nx][ny] == PAPER_ROLL
                        ) {
                            toCheck.add(Point(nx, ny))
                        }
                    }
                }
            }
        }
        return count
    }

    private fun findAllAccessiblePoints(paperRollMap: Array<CharArray>): List<Point> {
        val pointList = mutableListOf<Point>()
        for (i in paperRollMap.indices) {
            for (j in paperRollMap[i].indices) {
                if (paperRollMap[i][j] == PAPER_ROLL) {
                    val currentLocation = Point(i, j)
                    val nbPaperRollsAround = paperRollMap.nbPaperRollAround(currentLocation)
                    if (nbPaperRollsAround < 4) {
                        pointList.add(currentLocation)
                    }
                }
            }
        }
        return pointList
    }

    fun Array<CharArray>.nbPaperRollAround(point: Point): Int {
        var count = 0
        val i = point.x
        val j = point.y
        val xRange = (i - 1).coerceAtLeast(0)..(i + 1).coerceAtMost(size - 1)
        //let's assume row size does not change for performance
        val yRange = (j - 1).coerceAtLeast(0)..(j + 1).coerceAtMost(this[0].size - 1)
        for (x in xRange) {
            for (y in yRange) {
                if ((x != i || y != j) && this[x][y] == PAPER_ROLL) {
                    ++count
                }
            }
        }
        return count
    }

    context(log: Log)
    fun Array<CharArray>.log() {
        for (i in this.indices) {
            log.d(this[i].joinToString(""))
        }
    }

    /***
     * Slower alternatives below. Input is probably too small to see improvements
     */
    private suspend fun CoroutineScope.findAllAccessiblePointsCoroutines(paperRollMap: Array<CharArray>): List<Point> {
        // 1 coroutine per row
        return paperRollMap.indices.map { i ->
            async(Dispatchers.Default) {
                paperRollMap[i].indices.mapNotNull { j ->
                    if (paperRollMap[i][j] != PAPER_ROLL) return@mapNotNull null

                    val currentLocation = Point(i, j)
                    val nbPaperRollsAround = paperRollMap.nbPaperRollAround(currentLocation)
                    if (nbPaperRollsAround < 4) {
                        Point(i, j)
                    } else null
                }
            }
        }.awaitAll().flatten()
    }

    private suspend fun CoroutineScope.findAllAccessiblePointsCoroutines2(paperRollMap: Array<CharArray>): List<Point> {
        // chunked
        val chunkSize = max(1, paperRollMap.size / (Runtime.getRuntime().availableProcessors() * 2))
        return paperRollMap.indices.chunked(chunkSize).map { rowIndices ->
            async(Dispatchers.Default) {
                val localResults = mutableListOf<Point>()
                for (i in rowIndices) {
                    for (j in paperRollMap[i].indices) {
                        if (paperRollMap[i][j] != PAPER_ROLL) continue

                        val currentLocation = Point(i, j)
                        val nbPaperRollsAround = paperRollMap.nbPaperRollAround(currentLocation)
                        if (nbPaperRollsAround < 4) {
                            localResults.add(currentLocation)
                        }
                    }
                }
                localResults
            }
        }.awaitAll().flatten()
    }

    private suspend fun findAllAccessiblePointWithCoroutineFlows(paperRollMap: Array<CharArray>): List<Point> {
        // 1 coroutine flow per row
        return paperRollMap.indices.asFlow()
            .flatMapMerge { i ->
                flow {
                    for (j in paperRollMap[i].indices) {
                        if (paperRollMap[i][j] != PAPER_ROLL) continue

                        val currentLocation = Point(i, j)
                        val nbPaperRollsAround = paperRollMap.nbPaperRollAround(currentLocation)
                        if (nbPaperRollsAround < 4) {
                            emit(currentLocation)
                        }
                    }
                }.flowOn(Dispatchers.Default)
            }.toList()
    }

    private suspend fun findAllAccessiblePointWithCoroutineFlows2(paperRollMap: Array<CharArray>): List<Point> {
        // chunked
        val chunkSize = max(1, paperRollMap.size / (Runtime.getRuntime().availableProcessors() * 2))
        return paperRollMap.indices.asFlow()
            .chunked(chunkSize)
            .flatMapMerge { rowIndices ->
                flow {
                    for (i in rowIndices) {
                        for (j in paperRollMap[i].indices) {
                            if (paperRollMap[i][j] != PAPER_ROLL) continue

                            val currentLocation = Point(i, j)
                            val nbPaperRollsAround = paperRollMap.nbPaperRollAround(currentLocation)
                            if (nbPaperRollsAround < 4) {
                                emit(currentLocation)
                            }
                        }
                    }
                }.flowOn(Dispatchers.Default)
            }.toList()
    }
}