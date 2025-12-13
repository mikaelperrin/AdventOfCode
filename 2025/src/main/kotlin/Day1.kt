package io.overclockmp

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.scan
import kotlin.math.abs
import kotlin.math.absoluteValue

enum class Direction(val encodedDirection: Char, val sign: Short) {
    Left('L', -1),
    Right('R', +1);

    companion object {
        fun decode(encodedDirection: Char): Direction {
            entries.forEach {
                if (encodedDirection == it.encodedDirection) {
                    return it
                }
            }
            throw IllegalArgumentException("Direction cannot be decoded")
        }
    }
}

data class Rotation(
    val direction: Direction,
    val offset: Int
)

fun String.toRotation(): Rotation {
    if (!this.matches("^[LR]\\d+$".toRegex()))
        throw IllegalArgumentException("Incorrect direction input: $this")

    val direction = Direction.decode(this[0])
    val offset = this.substring(startIndex = 1).toInt()
    return Rotation(direction, offset)
}

data class DialState(val position: Int, val zeroPassCountForRotation: Int = 0)

object Day1 {
    private val log = Log()
    suspend fun findPassword(
        rotations: List<String>,
        initialDialPosition: Int = 50,
        use0x434C49434BMethod: Boolean = true,
    ): Int {
        val dialStates = rotations.asFlow()
            .map { it.toRotation() }
            .scan(DialState(initialDialPosition)) { dial, rotation ->
                calculateNewDialState(dial, rotation)
            }
        return if (use0x434C49434BMethod) {
            dialStates.map { it.zeroPassCountForRotation }
                .reduce { zeroPassCount, rotations ->
                    zeroPassCount + rotations
                }
        } else {
            dialStates.count { it.position == 0 }
        }
    }

    private fun calculateNewDialState(
        dialState: DialState,
        rotation: Rotation
    ): DialState {
        val signedOffset = rotation.direction.sign * rotation.offset
        val unboundPosition = dialState.position + signedOffset
        val newPosition = unboundPosition.mod(100) //same as floorMod(unboundPosition, 100)
        // count the number of time we did a full rotation
        // We also add 1 if the unbound position is below 0 (which means we crossed 0 an additional time)
        // But do not add 1 if the original position was 0 (since we did not technically "crossed" it)
        val zeroPassCount = unboundPosition.div(100).absoluteValue +
                (unboundPosition <= 0 && dialState.position != 0).toInt()
        log.d("$rotation -> New position: $newPosition & zeroPassCount: $zeroPassCount")
        return dialState.copy(position = newPosition, zeroPassCountForRotation = zeroPassCount)
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}