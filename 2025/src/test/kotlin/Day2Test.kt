import io.overclockmp.Day2.addAllInvalidIds
import io.overclockmp.Log
import io.overclockmp.StringRange
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day2Test {

    private val log = Log()

    @Test
    fun `(part 1) test sum of all invalid ids with sample`() = runTest {
        val idsSum = with(log) {
            addAllInvalidIds(SAMPLE_INPUT)
        }
        assertEquals(expected = 1227775554u, actual = idsSum)
    }

    @Test
    fun `(part 1) test sum of all invalid ids with real data`() = runTest {
        val input = getFileContent("day2_input").first()
        val result = with(log) {
            addAllInvalidIds(input)
        }
        assertEquals(expected = 55916882972u, actual = result)
    }

    @Test
    fun `(part 2) test sum of all invalid ids with sample`() = runTest {
        val idsSum = with(log) {
            addAllInvalidIds(SAMPLE_INPUT_PART2, supportNDuplicates = true)
        }
        assertEquals(expected = 4174379265u, actual = idsSum)
    }

    @Test
    fun `(part 2) test sum of all invalid ids with real data`() = runTest {
        val input = getFileContent("day2_input").first()
        val result = with(log) {
            addAllInvalidIds(input, supportNDuplicates = true)
        }
        assertEquals(expected = 76169125915u, actual = result)
    }

    @Test
    fun `string range first`() {
        val stringRange = StringRange("8", "21")
        assertEquals("8", stringRange.toList().first())
    }

    @Test
    fun `string range last`() {
        val stringRange = StringRange("8", "21")
        assertEquals("21", stringRange.toList().last())
    }

    @Test
    fun `string range list`() {
        val stringRange = StringRange("8", "12")
        assertEquals(listOf("8", "9", "10", "11", "12"), stringRange.toList())
    }


    private companion object {
        const val SAMPLE_INPUT = "11-22,95-115,998-1012,1188511880-1188511890," +
                "222220-222224,1698522-1698528,446443-446449,38593856-38593862"

        const val SAMPLE_INPUT_PART2 = "11-22,95-115,998-1012,1188511880-1188511890," +
                "222220-222224,1698522-1698528,446443-446449,38593856-38593862," +
                "565653-565659,824824821-824824827,2121212118-2121212124"
    }
}