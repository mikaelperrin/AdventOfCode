package io.overclockmp

import java.lang.IllegalStateException

data class Node(
    val line: Int,
    val column: Int,
    val children: MutableMap<String, Node> = mutableMapOf()
) {
    fun addChild(node: Node): Node {
        children["${node.line}-${node.column}"] = node
        return node
    }

    context(log: Log)
    fun countCombinations(): Long { // too slow with the real input
        return if(children.isEmpty()) {
            return 1
        } else {
            children.values.sumOf { it.countCombinations() }
        }
    }

    context(log: Log)
    fun countCombinationsMemo(memo: MutableMap<String, Long> = mutableMapOf()): Long {
        val key = "$line-$column"
        memo[key]?.let { return it }

        val result = if (children.isEmpty()) {
            1
        } else {
            children.values.sumOf { it.countCombinationsMemo(memo) }
        }

        memo[key] = result
        return result
    }
}

object Day7 {
    context(log:Log)
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
                if(emptyOrSplitter == '^') {
                    splitCount++
                    if(beamLocation-1 >= 0) {
                        newBeams.add(beamLocation-1)
                    }
                    if(beamLocation+1 < diagramSize) {
                        newBeams.add(beamLocation+1)
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


        val tree = Node(0, startLocation)
        val nodeIndex = mutableMapOf("0-$startLocation" to tree)
        var beams = mutableSetOf(startLocation)

        for (lineIndex in 1..diagram.lastIndex) {
            val newBeams = mutableSetOf<Int>()

            beams.forEach { beamLocation ->
                val emptyOrSplitter = diagram[lineIndex][beamLocation]

                if(emptyOrSplitter == '^') {
                    val leftChild = beamLocation - 1
                    if(leftChild >= 0) {
                        newBeams.add(leftChild)
                        nodeIndex.addChildNode(
                            lineIndex-1,
                            beamLocation,
                            lineIndex,
                            leftChild
                        )
                    }

                    val rightChild = beamLocation + 1
                    if(rightChild < diagramSize) {
                        newBeams.add(rightChild)
                        nodeIndex.addChildNode(
                            lineIndex-1,
                            beamLocation,
                            lineIndex,
                            rightChild
                        )
                    }
                } else {
                    newBeams.add(beamLocation)
                    nodeIndex.addChildNode(
                        lineIndex-1,
                        beamLocation,
                        lineIndex,
                        beamLocation
                    )
                }
            }

            beams = newBeams
            log.traceLine(diagram[lineIndex], beams.toSet())
        }


        return tree.countCombinationsMemo()
    }

    private fun MutableMap<String, Node>.addChildNode(
        parentLineIndex: Int,
        parentBeamLocation: Int,
        childLineIndex: Int,
        childBeamLocation: Int
    ) {
        val childNode = this.getOrPut("$childLineIndex-$childBeamLocation") {
            Node(childLineIndex, childBeamLocation)
        }
        this["$parentLineIndex-$parentBeamLocation"]?.addChild(childNode)
            ?: throw IllegalStateException("Cannot find Node($parentLineIndex,$parentBeamLocation) in map")
    }

    private fun Log.traceLine(line: String, beamSet: Set<Int>) {
        val modifiedLine = line.indices.map {
            if(it in beamSet) {
                '|'
            } else {
                line[it]
            }
        }
        d(modifiedLine.joinToString(""))
    }
}
