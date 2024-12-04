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

        /**
         * Returns null if the report is valid, or the index of the first invalid number.
         */
        fun checkReport(report: List<Int>): Int? {
            var wasIncreasing: Boolean? = null
            var idx = 0
            for ((level0, level1) in report.windowed(2)) {
                val isIncreasing = when (level1 - level0) {
                    in 1..3 -> true
                    in -3..-1 -> false
                    else -> return idx
                }
                when {
                    wasIncreasing == null -> wasIncreasing = isIncreasing
                    wasIncreasing != isIncreasing -> return idx
                }
                idx++
            }
            return null
        }

        var count = 0

        reports.forEach { report ->
            val invalidIdx = checkReport(report)
            if (invalidIdx == null) {
                count++
                return@forEach
            }
            val report1 = report.toMutableList().apply { removeAt(invalidIdx) }
            if (checkReport(report1) == null) {
                "Removed ${report[invalidIdx]} at $invalidIdx from $report".println()
                count++
                return@forEach
            }
            val report2 = report.toMutableList().apply { removeAt(invalidIdx + 1) }
            if (checkReport(report2) == null) {
                "Removed ${report[invalidIdx + 1]} at ${invalidIdx + 1} from $report".println()
                count++
                return@forEach
            }
        }

        return count
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
