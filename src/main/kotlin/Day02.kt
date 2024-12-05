import kotlin.system.measureTimeMillis

fun main() {

    fun parseInputToIntList(input: List<String>) = input.map { it.split(Regex("\\s+")).map { n -> n.toInt() } }

    fun part1(input: List<String>): Int {
        val reports = parseInputToIntList(input)
        return reports.count { report ->
            var wasIncreasing: Boolean? = null
            for ((level0, level1) in report.windowed(2)) {
                val isIncreasing = when (level1 - level0) {
                    in 1..3 -> true
                    in -3..-1 -> false
                    else -> return@count false
                }
                when {
                    wasIncreasing == null -> wasIncreasing = isIncreasing
                    wasIncreasing != isIncreasing -> return@count false
                }
            }
            return@count true
        }
    }

    fun part2(input: List<String>): Int {
        val reports = parseInputToIntList(input)

        fun checkReport(report: List<Int>): Boolean {
            var wasIncreasing: Boolean? = null
            for ((level0, level1) in report.windowed(2)) {
                val isIncreasing = when (level1 - level0) {
                    in 1..3 -> true
                    in -3..-1 -> false
                    else -> return false
                }
                when {
                    wasIncreasing == null -> wasIncreasing = isIncreasing
                    wasIncreasing != isIncreasing -> return false
                }
            }
            return true
        }

        return reports.count { report ->
            if (checkReport(report)) return@count true
            for (i in report.indices) {
                val report1 = report.toMutableList().apply { removeAt(i) }
                if (checkReport(report1)) return@count true
            }
            return@count false
        }
    }

    val testInput = readInput("02t")
    val input = readInput("02")

    check(part1(testInput) == 2)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 4)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
