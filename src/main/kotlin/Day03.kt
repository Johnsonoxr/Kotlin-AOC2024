import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Int {
        val regex = Regex("mul\\(\\d+,\\d+\\)")
        val found = regex.findAll(input.joinToString { it })
        val pairs = found.map { it.value.drop(4).dropLast(1).split(",").map { nStr -> nStr.toInt() } }
        return pairs.sumOf { (a, b) -> a * b }
    }

    fun part2(input: List<String>): Int {
        val regex = Regex("mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)")
        val found = regex.findAll(input.joinToString { it })
        var sum = 0
        var doCurrent = true
        found.forEach { f ->
            when (f.value) {
                "do()" -> doCurrent = true
                "don't()" -> doCurrent = false
                else -> {
                    val (a, b) = f.value.drop(4).dropLast(1).split(",").map { nStr -> nStr.toInt() }
                    if (doCurrent) sum += a * b
                }
            }
        }
        return sum
    }

    val testInput = readInput("03t")
    val testInput2 = readInput("03t2")
    val input = readInput("03")

    check(part1(testInput) == 161)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput2) == 48)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
