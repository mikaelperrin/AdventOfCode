import io.overclockmp.Day6
import io.overclockmp.Day6.solveProblemsPart1
import io.overclockmp.Day6.solveProblemsPart2
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day6Test {


    @Test
    fun `(part 1) test sum of solved math problems with sample data`() {
        val mathResult = solveProblemsPart1(SAMPLE_INPUT)
        assertEquals(expected = 4277556L, mathResult)
    }

    @Test
    fun `(part 1) test sum of solved math problems with real data`() = runTest {
        val lines = getFileContent("day6_input")
        val mathResult = solveProblemsPart1(lines)
        assertEquals(expected = 4309240495780L, mathResult)
    }

    @Test
    fun `(part 2) test sum of solved math problems with sample data`() {
        val mathResult = solveProblemsPart2(SAMPLE_INPUT)
        assertEquals(expected = 3263827L, mathResult)
    }


    @Test
    fun `(part 2) test sum of solved math problems with real data`() = runTest {
        val lines = getFileContent("day6_input")
        val mathResult = solveProblemsPart2(lines)
        assertEquals(expected = 9170286552289L, mathResult)
    }

    private companion object {
        val SAMPLE_INPUT = listOf(
            "123 328  51 64 ",
            " 45 64  387 23 ",
            "  6 98  215 314",
            "*   +   *   +  "
        )
    }
}