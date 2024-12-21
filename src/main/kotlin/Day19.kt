import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Int {
        val towels = input.first().split(", ")
        val designs = input.drop(2)

        val cache = mutableMapOf<String, Boolean>()

        fun String.isDesignPossible(): Boolean {
            cache[this]?.let { return it }
            if (this in towels) return true
            val matchedTowels = towels.filter { this.startsWith(it) }.takeIf { it.isNotEmpty() } ?: return false
            return matchedTowels.any { this.removePrefix(it).isDesignPossible() }.also { cache[this] = it }
        }

        return designs.count { it.isDesignPossible() }
    }

    fun part2(input: List<String>): Long {
        val towels = input.first().split(", ")
        val designs = input.drop(2)

        val cache = mutableMapOf<String, Long>()

        fun String.countPossibleDesigns(): Long {
            if (this.isEmpty()) return 1
            cache[this]?.let { return it }
            val matchedTowels = towels.filter { this.startsWith(it) }.takeIf { it.isNotEmpty() } ?: return 0
            return matchedTowels.sumOf { this.removePrefix(it).countPossibleDesigns() }.also { cache[this] = it }
        }

        return designs.sumOf { it.countPossibleDesigns() }
    }

    val testInput = readInput("19t")
    val input = readInput("19")

    check(part1(testInput) == 6)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 16L)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
