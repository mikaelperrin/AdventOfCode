import io.overclockmp.Day1.findPassword
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day1Test {

    private val log = Log()

    @Test
    fun `when rotating left and new position is 0, findPassword return 1`() = runTest {
        val password = findPassword(arrayOf("L50"), initialDialPosition = 50)
        assertEquals(expected = 1, actual = password)
    }

    @Test
    fun `when rotating right and new position is 0, findPassword return 1`() = runTest {
        val password = findPassword(arrayOf("R50"), initialDialPosition = 50)
        assertEquals(expected = 1, actual = password)
    }

    @Test
    fun `when rotating left and new position is above 0, find password returns 0`() = runTest {
        val password = findPassword(arrayOf("L10"), initialDialPosition = 50)
        assertEquals(expected = 0, actual = password)
    }

    @Test
    fun `when rotating left and new position is below 0, find password returns 1`() = runTest {
        val password = findPassword(arrayOf("L60"), initialDialPosition = 50)
        assertEquals(expected = 1, actual = password)
    }

    @Test
    fun `when rotating left and new position is below 100, find password returns 2`() = runTest {
        val password = findPassword(arrayOf("L160"), initialDialPosition = 50)
        assertEquals(expected = 2, actual = password)
    }

    @Test
    fun `when rotating right and new position is below 100, find password returns 0`() = runTest {
        val password = findPassword(arrayOf("R10"), initialDialPosition = 50)
        assertEquals(expected = 0, actual = password)
    }

    @Test
    fun `when rotating right and new position is above 100, find password returns 1`() = runTest {
        val password = findPassword(arrayOf("R60"), initialDialPosition = 50)
        assertEquals(expected = 1, actual = password)
    }

    @Test
    fun `when rotating right and new position is above 200, find password returns 2`() = runTest {
        val password = findPassword(arrayOf("R160"), initialDialPosition = 50)
        assertEquals(expected = 2, actual = password)
    }

    @Test
    fun findPasswordPart1WithSample() = runTest {
        val password = findPassword(SAMPLE_ROTATIONS, use0x434C49434BMethod = false)
        assertEquals(expected = 3, actual = password)
    }

    @Test
    fun findPasswordPart1WithRealInput() = runTest {
        val lines = getFileContent("day1_input")
        val password = findPassword(lines.toTypedArray(), use0x434C49434BMethod = false)
        assertEquals(expected = 1100, actual = password)
    }

    @Test
    fun findPasswordPart2WithSample() = runTest {
        val password = findPassword(SAMPLE_ROTATIONS)
        assertEquals(expected = 6, actual = password)
    }

    @Test
    fun findPasswordPart2WithRealInput() = runTest {
        val lines = getFileContent("day1_input")
        val password = context(log) {
            findPassword(lines.toTypedArray())
        }
        assertEquals(expected = 6358, actual = password)
    }

    private companion object {
        val SAMPLE_ROTATIONS = arrayOf(
            "L68",
            "L30",
            "R48",
            "L5",
            "R60",
            "L55",
            "L1",
            "L99",
            "R14",
            "L82",
        )
    }

}