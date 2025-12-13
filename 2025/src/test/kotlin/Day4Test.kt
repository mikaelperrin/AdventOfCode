import io.overclockmp.Day4.findNumberOfAccessiblePaper
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day4Test {

    private val log = Log()

    @Test
    fun `(part 1) test the number of accessible paper with sample data`() = runTest {
        val actualAccessiblePaper = with(log) {
            findNumberOfAccessiblePaper(SAMPLE_INPUT)
        }
        assertEquals(expected = 13, actual = actualAccessiblePaper)
    }

    @Test
    fun `(part 1) test the number of accessible paper with real data`() = runTest {
        val lines = getFileContent("day4_input")
        val actualAccessiblePaper = with(log) {
            findNumberOfAccessiblePaper(lines)
        }
        assertEquals(expected = 1523, actual = actualAccessiblePaper)
    }

    @Test
    fun `(part 2) test the number of accessible paper with sample data`() = runTest {
        val actualAccessiblePaper = with(log) {
            findNumberOfAccessiblePaper(SAMPLE_INPUT, keepGoing = true)
        }
        assertEquals(expected = 43, actual = actualAccessiblePaper)
    }

    @Test
    fun `(part 2) test the number of accessible paper with real data`() = runTest {
        val lines = getFileContent("day4_input")
        val actualAccessiblePaper = with(log) {
            findNumberOfAccessiblePaper(lines, keepGoing = true)
        }
        assertEquals(expected = 9290, actual = actualAccessiblePaper)
    }

    private companion object {
        val SAMPLE_INPUT = listOf<String>(
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        )
    }

}