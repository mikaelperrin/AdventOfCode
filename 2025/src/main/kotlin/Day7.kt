package io.overclockmp

data class Position(val line: Int, val column: Int)

object Day7 {
    context(log: Log)
    fun runTeleporterTachyonBeam(diagram: List<String>): Int {
        val firstLine = diagram.first()
        val diagramSize = firstLine.length
        val startLocation = firstLine.indexOf('S')
        var beams = mutableSetOf(startLocation)
        var splitCount = 0
        for (lineIndex in 1..<diagram.lastIndex) {
            val newBeams = mutableSetOf<Int>()
            beams.forEach { beamLocation ->
                val emptyOrSplitter = diagram[lineIndex][beamLocation]
                if (emptyOrSplitter == '^') {
                    splitCount++
                    if (beamLocation - 1 >= 0) {
                        newBeams.add(beamLocation - 1)
                    }
                    if (beamLocation + 1 < diagramSize) {
                        newBeams.add(beamLocation + 1)
                    }
                } else {
                    newBeams.add(beamLocation)
                }
            }
            beams = newBeams
            log.traceLine(diagram[lineIndex], beams.toSet())
        }
        return splitCount
    }

    context(log: Log)
    fun runTeleporterTachyonBeamPart2(diagram: List<String>): Long {
        val firstLine = diagram.first()
        val diagramSize = firstLine.length
        val startLocation = firstLine.indexOf('S')

        var nodeToPathCountIndex = mutableMapOf(Position(0, startLocation) to 1L) //Position to Count

        for (lineIndex in 1..<diagram.lastIndex) {
            val newNodeToPathCountIndex = mutableMapOf<Position, Long>()

            nodeToPathCountIndex.forEach { (position, pathCount) ->
                val beamLocation = position.column
                val emptyOrSplitter = diagram[lineIndex][beamLocation]

                if (emptyOrSplitter == '^') {
                    val leftChild = beamLocation - 1
                    if (leftChild >= 0) {
                        val leftKey = Position(lineIndex, leftChild)
                        newNodeToPathCountIndex[leftKey] = newNodeToPathCountIndex.getOrDefault(leftKey, 0L) + pathCount
                    }

                    val rightChild = beamLocation + 1
                    if (rightChild < diagramSize) {
                        val rightKey = Position(lineIndex, rightChild)
                        newNodeToPathCountIndex[rightKey] =
                            newNodeToPathCountIndex.getOrDefault(rightKey, 0L) + pathCount
                    }
                } else {
                    val straightKey = Position(lineIndex, beamLocation)
                    newNodeToPathCountIndex[straightKey] =
                        newNodeToPathCountIndex.getOrDefault(straightKey, 0L) + pathCount
                }
            }

            nodeToPathCountIndex = newNodeToPathCountIndex
            log.traceLine(diagram[lineIndex], nodeToPathCountIndex.keys.map { it.column }.toSet())
        }


        return nodeToPathCountIndex.values.sum()
    }

    private fun Log.traceLine(line: String, beamSet: Set<Int>) {
        val modifiedLine = line.indices.map {
            if (it in beamSet) {
                '|'
            } else {
                line[it]
            }
        }
        d(modifiedLine.joinToString(""))
    }
}
