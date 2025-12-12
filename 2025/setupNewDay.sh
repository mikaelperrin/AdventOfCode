#!/usr/bin/env bash

set -euo pipefail

if [[ $# -ne 1 ]]; then
    echo "Usage: $0 <day number>" >&2
    echo "Must provide a day number" >&2
    exit 1
fi

if ! [[ $1 =~ ^[0-9]+$ ]]; then
    echo "Error: Day number must be numeric" >&2
    exit 1
fi

DAY_NUMBER="$1"
SOURCE_FILE="./src/main/kotlin/Day${DAY_NUMBER}.kt"
TEST_FILE="./src/test/kotlin/Day${DAY_NUMBER}Test.kt"
RESOURCE_FILE="./src/main/resources/day${DAY_NUMBER}_input"

if [[ -e $SOURCE_FILE ]]; then
    echo "Source file for that day already exists" >&2
    exit 2
fi

if [[ -e $TEST_FILE ]]; then
    echo "Test file for that day already exists" >&2
    exit 3
fi

if [[ -e $RESOURCE_FILE ]]; then
    echo "Resource file for that day already exists" >&2
    exit 4
fi

cat <<EOF > $SOURCE_FILE
package io.overclockmp

object Day${DAY_NUMBER} {

}
EOF



cat <<EOF > $TEST_FILE
import io.overclockmp.Log
import io.overclockmp.getFileContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day${DAY_NUMBER}Test {

    private val log = Log()

    @Test
    fun \`(part 1) test with sample input\`() {

        //assertEquals(expected = , actual = )
    }

    @Test
    fun \`(part 1) test with real input\`() = runTest {
        val lines = getFileContent("day${DAY_NUMBER}_input")

        //assertEquals(expected = , actual = )
    }

    @Test
    fun \`(part 2) test with sample input\`() {

        //assertEquals(expected = , actual = )
    }

    @Test
    fun \`(part 2) test with real input\`() = runTest {
        val lines = getFileContent("day${DAY_NUMBER}_input")

        //assertEquals(expected = , actual = )
    }

    private companion object {
        val SAMPLE_INPUT = listOf(

        )
    }
}
EOF

touch $RESOURCE_FILE

echo "Successfully created files for Day ${DAY_NUMBER}:"
echo "  - $SOURCE_FILE"
echo "  - $TEST_FILE"
echo "  - $RESOURCE_FILE"
