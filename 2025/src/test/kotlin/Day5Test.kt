import io.overclockmp.Day5
import io.overclockmp.Day5.findAllFreshProductIDs
import io.overclockmp.Day5.findNumberOfFreshProductsFromInventory
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {

    private val log = Log()
    @Test
    fun `(part 1) test number of fresh products with sample input`() = runTest {
        val numberOfFreshProducts = with(log) {
            findNumberOfFreshProductsFromInventory(
                SAMPLE_INPUT_RANGES,
                SAMPLE_INPUT_PRODUCTIDS
            )
        }
        assertEquals(expected = 3, actual = numberOfFreshProducts)
    }

    @Test
    fun `(part 1) test number of fresh products with real input`() = runTest {
        val lines = getFileContent("day5_input")
        val separatorLineIndex = lines.indexOfFirst{ it.isEmpty() }
        val ranges = lines.take(separatorLineIndex)
        val productIDs = lines.drop(separatorLineIndex + 1)
        val numberOfFreshProducts = with(log) {
            findNumberOfFreshProductsFromInventory(ranges, productIDs)
        }
        assertEquals(expected = 694, actual = numberOfFreshProducts)
    }

    @Test
    fun `(part 2) test total number of fresh products with sample input`() = runTest {
        val numberOfFreshProducts = with(log) {
            findAllFreshProductIDs(SAMPLE_INPUT_RANGES)
        }
        assertEquals(expected = 14, actual = numberOfFreshProducts)
    }

    @Test
    fun `(part 2) test total number of fresh products with real input`() = runTest {
        val lines = getFileContent("day5_input")
        val separatorLineIndex = lines.indexOfFirst{ it.isEmpty() }
        val ranges = lines.take(separatorLineIndex)
        val numberOfFreshProducts = with(log) {
            findAllFreshProductIDs(ranges)
        }
        assertEquals(expected = 352716206375547, actual = numberOfFreshProducts)
    }

    private companion object {
        val SAMPLE_INPUT_RANGES = listOf(
            "3-5",
            "10-14",
            "16-20",
            "12-18",
            "13-15"
        )
        val SAMPLE_INPUT_PRODUCTIDS = listOf(
            "1",
            "5",
            "8",
            "11",
            "17",
            "32",
        )
    }

}