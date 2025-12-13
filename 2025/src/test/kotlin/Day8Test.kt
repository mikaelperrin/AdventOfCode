import io.overclockmp.Day8.connectJunctionBoxes
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8Test {

    private val log = Log()

    @Test
    fun `(part 1) test with sample input`() = runTest {
        val result = with(log) {
            connectJunctionBoxes(SAMPLE_INPUT, 10)
        }
        assertEquals(expected = 40L, actual = result)
    }

    @Test
    fun `(part 1) test with real input`() = runTest {
        val lines = getFileContent("day8_input")
        val result = with(log) {
            connectJunctionBoxes(lines, 1000)
        }
        assertEquals(expected = 97384, actual = result)
    }

    @Test
    fun `(part 2) test with sample input`() = runTest {
        val result = with(log) {
            connectJunctionBoxes(SAMPLE_INPUT)
        }
        assertEquals(expected = 25272L, actual = result)
    }

    @Test
    fun `(part 2) test with real input`() = runTest {
        val lines = getFileContent("day8_input")
        val result = with(log) {
            connectJunctionBoxes(lines)
        }
        assertEquals(expected = 9003685096L, actual = result)
    }

    private companion object {
        val SAMPLE_INPUT = listOf(
            "162,817,812",  // 425,690,689 -> 316.90219311326956
            "57,618,57",    // 466,668,158 -> 424.24285497813634
            "906,360,560",  // 805,96,715 -> 322.36935338211043
            "592,479,940",  // 425,690,689 -> 367.9823365326113
            "352,342,300",  // 542,29,236 -> 371.70552861102294
            "466,668,158",  // 352,342,300 -> 373.41130138226936
            "542,29,236",   // 352,342,300-> 371.70552861102294
            "431,825,988",  // 162,817,812 -> 321.560258738545
            "739,650,466",  // 906,360,560 -> 347.59890678769403
            "52,470,668",   // 117,168,530 -> 338.33858780813046
            "216,146,977",  // 117,168,530 -> 458.360120429341
            "819,987,18",   // 941,993,340 -> 344.3893145845266
            "117,168,530",  // 52,470,668 -> 338.33858780813046
            "805,96,715",   // 906,360,560 -> 322.36935338211043
            "346,949,466",  // 425,690,689 -> 350.786259708102
            "970,615,88",   // 819,987,18 -> 407.53527454687895
            "941,993,340",  // 819,987,18 -> 344.3893145845266
            "862,61,35",    // 984,92,344-> 333.6555109690233
            "984,92,344",   // 862,61,35 -> 333.6555109690233
            "425,690,689"   // 162,817,812 -> 316.90219311326956
        )
        /**
         * Box(x=162, y=817, z=812)=(Box(x=425, y=690, z=689), 316.90219311326956)
         * Box(x=425, y=690, z=689)=(Box(x=162, y=817, z=812), 316.90219311326956)
         * Box(x=431, y=825, z=988)=(Box(x=162, y=817, z=812), 321.560258738545)
         * Box(x=906, y=360, z=560)=(Box(x=805, y=96, z=715), 322.36935338211043)
         * Box(x=805, y=96, z=715)=(Box(x=906, y=360, z=560), 322.36935338211043)
         * Box(x=862, y=61, z=35)=(Box(x=984, y=92, z=344), 333.6555109690233)
         * Box(x=984, y=92, z=344)=(Box(x=862, y=61, z=35), 333.6555109690233)
         * Box(x=52, y=470, z=668)=(Box(x=117, y=168, z=530), 338.33858780813046)
         * Box(x=117, y=168, z=530)=(Box(x=52, y=470, z=668), 338.33858780813046)
         * Box(x=819, y=987, z=18)=(Box(x=941, y=993, z=340), 344.3893145845266)
         * Box(x=941, y=993, z=340)=(Box(x=819, y=987, z=18), 344.3893145845266)
         * Box(x=739, y=650, z=466)=(Box(x=906, y=360, z=560), 347.59890678769403)
         * Box(x=346, y=949, z=466)=(Box(x=425, y=690, z=689), 350.786259708102)
         * Box(x=592, y=479, z=940)=(Box(x=425, y=690, z=689), 367.9823365326113)
         * Box(x=542, y=29, z=236)=(Box(x=352, y=342, z=300), 371.70552861102294)
         * Box(x=352, y=342, z=300)=(Box(x=542, y=29, z=236), 371.70552861102294)
         * Box(x=466, y=668, z=158)=(Box(x=352, y=342, z=300), 373.41130138226936)
         * Box(x=970, y=615, z=88)=(Box(x=819, y=987, z=18), 407.53527454687895)
         * Box(x=57, y=618, z=57)=(Box(x=466, y=668, z=158), 424.24285497813634)
         * Box(x=216, y=146, z=977)=(Box(x=117, y=168, z=530), 458.360120429341)
         */
    }
}
