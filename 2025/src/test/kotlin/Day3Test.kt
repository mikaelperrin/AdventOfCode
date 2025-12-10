import io.overclockmp.Day3.findHighestOutputJoltage
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day3Test {

    private val log = Log()

    @Test
    fun `(part 1) test finding the max joltage for one battery bank`() = runTest {
        with(log) {
            val highestJoltage = findHighestOutputJoltage(listOf("234234234234278"))
            assertEquals(expected = 78, actual = highestJoltage)
        }
    }

    @Test
    fun `(part 2) test finding the max joltage for one battery bank`() = runTest {
        with(log) {
            val highestJoltage = findHighestOutputJoltage(listOf("234234234234278"), batteryCount = 12)
            assertEquals(expected = 434234234278L, actual = highestJoltage)
        }
    }

    @Test
    fun `(part 1) finding the max joltage with sample`() = runTest {
        with(log) {
            val highestJoltage = findHighestOutputJoltage(SAMPLE_INPUT)
            assertEquals(expected = 357, actual = highestJoltage)
        }
    }

    @Test
    fun `(part 1) finding the max joltage with real input`() = runTest {
        val lines = getFileContent("day3_input")
        with(log) {
            val highestJoltage = findHighestOutputJoltage(lines)
            assertEquals(expected = 17324, actual = highestJoltage)
        }
    }

    @Test
    fun `(part 2) finding the max joltage with sample`() = runTest {
        with(log) {
            val highestJoltage = findHighestOutputJoltage(SAMPLE_INPUT, batteryCount = 12)
            assertEquals(expected = 3121910778619L, actual = highestJoltage)
        }
    }

    @Test
    fun `(part 2) finding the max joltage with real input`() = runTest {
        val lines = getFileContent("day3_input")
        with(log) {
            val highestJoltage = findHighestOutputJoltage(lines, batteryCount = 12)
            assertEquals(expected = 171846613143331L, actual = highestJoltage)
        }
    }

    private companion object {
        val SAMPLE_INPUT = listOf(
            "987654321111111",
            "811111111111119",
            "234234234234278",
            "818181911112111"
        )
    }
}