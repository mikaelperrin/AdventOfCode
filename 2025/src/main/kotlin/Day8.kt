@file:OptIn(ExperimentalCoroutinesApi::class)

package io.overclockmp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toSet
import kotlin.math.pow
import kotlin.math.sqrt

data class Box(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun distanceWith(other: Box) : Double {
        return sqrt((x-other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2))
    }

    fun connectTo(other: Box): Circuit {
        return Circuit(this).apply {
            connectTo(other)
        }
    }
}

data class Connection(
    val origin: Box,
    val destination: Box,
    val distance: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Connection

        if (distance != other.distance) return false

        return origin == other.origin && destination == other.destination ||
                origin == other.destination && destination == other.origin
    }

    override fun hashCode(): Int {
        var result = distance.hashCode()
        result = 31 * result + origin.hashCode() + destination.hashCode()
        return result
    }
}

class Circuit(origin: Box) {
    internal val junctionBoxes = mutableSetOf(origin)

    fun connectTo(junctionBox: Box): Circuit {
        if (junctionBox in junctionBoxes) {
            throw IllegalArgumentException("trying to add a junction box $junctionBox " +
                    "to a circuit that already contains it: $this")
        }
        junctionBoxes.add(junctionBox)
        return this
    }

    fun connectTo(other: Circuit): Circuit {
        junctionBoxes.addAll(other.junctionBoxes)
        return this
    }

    fun contains(junctionBox: Box) : Boolean {
        return junctionBoxes.contains(junctionBox)
    }

    fun size(): Int = junctionBoxes.size

    override fun toString(): String {
        return "Circuit(junctionBoxes=\n\t${junctionBoxes.joinToString("\n\t")}\n)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Circuit

        return junctionBoxes == other.junctionBoxes
    }

    override fun hashCode(): Int {
        return junctionBoxes.hashCode()
    }
}


private fun Int.pow(n: Int): Double {
    return this.toDouble().pow(n)
}

object Day8 {
    context(log: Log)
    suspend fun connectJunctionBoxes(lines: List<String>, maxNumberOfConnections: Int = 0): Long {
        val junctionBoxes = lines.map {line ->
            val parts = line.split(",\\s?".toRegex()).map { it.toInt() }
            Box(parts[0], parts[1], parts[2])
        }

        val lookingForNumberOfConnectionThatFormsASingleGroup = maxNumberOfConnections == 0

        val sortedConnections = junctionBoxes.createSortedListOfPossibleConnections().let { list ->
            if (lookingForNumberOfConnectionThatFormsASingleGroup) {
                list
            } else {
                list.take(maxNumberOfConnections)
            }
        }

        val circuitList = mutableListOf<Circuit>()
        var resultConnection: Connection? = null

        for (connection in sortedConnections) {
            val (origin, destination, _) = connection

            val originCircuit = circuitList.find { it.contains(origin) }?.also {
                circuitList.remove(it)
            } ?: Circuit(origin)

            val destinationCircuit = circuitList.find { it.contains(destination) }?.also {
                circuitList.remove(it)
            } ?: Circuit(destination)

            if(originCircuit == destinationCircuit) {
                continue
            }

            val newCircuit = originCircuit.connectTo(destinationCircuit)

            circuitList.add(newCircuit)

            if(lookingForNumberOfConnectionThatFormsASingleGroup
                && circuitList.size == 1
                && junctionBoxes.size == circuitList.first().size()) {
                resultConnection = connection
                break
            }
        }

        log.d("Final Circuit set: ${circuitList.sortedByDescending { it.size() }.joinToString()}")

        return if (lookingForNumberOfConnectionThatFormsASingleGroup) {
            if(resultConnection != null) {
                1L * resultConnection.origin.x * resultConnection.destination.x
            } else {
                0
            }
        } else {
            circuitList.sortedByDescending { it.size() }.take(3).fold(1L) { result, circuit ->
                result * circuit.size()
            }
        }
    }
}

private suspend fun List<Box>.createSortedListOfPossibleConnections(): List<Connection> {
    return this.asFlow()
        .flatMapMerge { origin ->
            flow {
                for (destination in this@createSortedListOfPossibleConnections) {
                    if (destination == origin) continue
                    val distance = origin.distanceWith(destination)
                    emit(Connection(origin, destination, distance))
                }
            }.flowOn(Dispatchers.Default)
        }.toSet()
        .sortedBy { (_, _, distance) ->
            distance
        }
}