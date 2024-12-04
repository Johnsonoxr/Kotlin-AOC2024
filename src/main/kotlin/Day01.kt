import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {

    data class Pair(val a: Int, val b: Int)

    fun toIntPairs(input: List<String>) = input.map { it.split(Regex("\\s+")).let { s -> Pair(s[0].toInt(), s[1].toInt()) } }

    fun part1(input: List<String>): Int {
        val pairs = toIntPairs(input)
        val (aList, bList) = pairs.map { it.a to it.b }.unzip()
        return aList.sorted().zip(bList.sorted()).sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        val pairs = toIntPairs(input)
        val (aList, bList) = pairs.map { it.a to it.b }.unzip()
        val bCountMap = bList.toSet().associateWith { b -> bList.count { it == b } }
        var sum = 0
        aList.forEach { a ->
            sum += a * (bCountMap[a] ?: 0)
        }
        return sum
    }

    val testInput = readInput("01t")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("01")
    val time1 = measureTimeMillis {
        part1(input).println()
    }
    val time2 = measureTimeMillis {
        part2(input).println()
    }
    "Completed in $time1 ms and $time2 ms".println()
}
