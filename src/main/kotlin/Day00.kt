import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("00t")
    check(part1(testInput) == 1)
    check(part2(testInput) == 1)

    val input = readInput("00")
    val time1 = measureTimeMillis {
        part1(input).println()
    }
    val time2 = measureTimeMillis {
        part2(input).println()
    }
    "Completed in $time1 ms and $time2 ms".println()
}
