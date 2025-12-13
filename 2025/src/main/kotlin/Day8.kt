@file:OptIn(ExperimentalCoroutinesApi::class)

package io.overclockmp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toSet
import kotlin.math.sqrt

data class Box(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun squaredDistanceWith(other: Box): Long {
        val dx = (x - other.x).toLong()
        val dy = (y - other.y).toLong()
        val dz = (z - other.z).toLong()
        return (dx * dx + dy * dy + dz * dz)
    }
    fun distanceWith(other: Box) : Double {
        return sqrt(squaredDistanceWith(other).toDouble())
    }
}

data class Connection(
    val origin: Box,
    val destination: Box,
    val distance: Long
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

    fun connectTo(other: Circuit): Circuit {
        junctionBoxes.addAll(other.junctionBoxes)
        return this
    }

    fun contains(junctionBox: Box) : Boolean {
        return junctionBoxes.contains(junctionBox)
    }

    fun size(): Int = junctionBoxes.size
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

    val connectionSet = sortedSetOf<Connection>(Comparator { c1, c2 -> c1.distance.compareTo(c2.distance) })

    for((i, origin) in this.withIndex()) {
        for(j in i+1..this.lastIndex) {
            val destination = this@createSortedListOfPossibleConnections[j]
            val distance = origin.squaredDistanceWith(destination)
            connectionSet.add(Connection(origin, destination, distance))
        }
    }

    return connectionSet.toList()
}