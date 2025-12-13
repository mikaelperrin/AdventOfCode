import io.overclockmp.Day7.runTeleporterTachyonBeam
import io.overclockmp.Day7.runTeleporterTachyonBeamPart2
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day7Test {

    private val log = Log()

    @Test
    fun `(part 1) test with sample input`() {
        val numberOfSplit = with(log) {
            runTeleporterTachyonBeam(SAMPLE_INPUT)
        }
        assertEquals(expected = 21, actual = numberOfSplit)
    }

    @Test
    fun `(part 1) test with real input`() = runTest {
        val lines = getFileContent("day7_input")
        val numberOfSplit = with(log) {
            runTeleporterTachyonBeam(lines)
        }
        assertEquals(expected = 1667, actual = numberOfSplit)
    }

    @Test
    fun `(part 2) test with sample input`() {
        val numberOfSplit = with(log) {
            runTeleporterTachyonBeamPart2(SAMPLE_INPUT)
        }
        assertEquals(expected = 40, actual = numberOfSplit)
    }

    @Test
    fun `(part 2) test with real input`() = runTest {
        val lines = getFileContent("day7_input")
        val numberOfSplit = with(log) {
            runTeleporterTachyonBeamPart2(lines)
        }
        assertEquals(expected = 62943905501815, actual = numberOfSplit)
    }

    private companion object {
        val SAMPLE_INPUT = listOf(
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
            ".....^.^.^.....",
            "...............",
            "....^.^...^....",
            "...............",
            "...^.^...^.^...",
            "...............",
            "..^...^.....^..",
            "...............",
            ".^.^.^.^.^...^.",
            "..............."
        )
    }

}
