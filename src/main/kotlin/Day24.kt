import kotlin.system.measureTimeMillis

fun main() {

    data class Connection(val src: Set<String>, val dst: String, val operation: String)

    fun part1(input: List<String>): Long {
        val initMap = input.take(input.indexOfFirst { it.isBlank() }).associate { it.split(": ").let { s -> s[0] to s[1].toInt() } }
        val connections = input.drop(input.indexOfFirst { it.isBlank() } + 1).map { line ->
            val (a, op, b, _, c) = line.split(" ")
            return@map Connection(setOf(a, b), c, op)
        }

        fun String.getOutput(): Int {
            initMap[this]?.let { return it }
            val connection = connections.first { it.dst == this }
            val srcValues = connection.src.map { it.getOutput() }
            return when (connection.operation) {
                "AND" -> if (srcValues.all { it == 1 }) 1 else 0
                "OR" -> if (srcValues.any { it == 1 }) 1 else 0
                "XOR" -> if (srcValues.count { it == 1 } == 1) 1 else 0
                else -> throw IllegalArgumentException("???")
            }
        }

        val binary = connections
            .asSequence()
            .filter { it.dst.startsWith('z') }
            .map { it.dst }
            .sortedByDescending { it.drop(1).toInt() }
            .map { it.getOutput() }
            .joinToString("")

        return binary.toLong(2)
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val testInput = readInput("24t")
    val input = readInput("24")

    check(part1(testInput) == 2024L)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 1)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
