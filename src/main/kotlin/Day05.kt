import kotlin.system.measureTimeMillis

fun main() {

    data class Rule(val left: Int, val right: Int)

    fun parseRulesAndUpdates(input: List<String>): Pair<List<Rule>, List<List<Int>>> {
        val emptyLineIdx = input.indexOf("")
        val ruleLines = input.subList(0, emptyLineIdx)
        val pageLines = input.subList(emptyLineIdx + 1, input.size)
        val rules = ruleLines.map { line -> line.split("|").let { Rule(it[0].toInt(), it[1].toInt()) } }
        val pages = pageLines.map { line -> line.split(",").map { it.toInt() } }
        return rules to pages
    }

    fun part1(input: List<String>): Int {
        val (rules, updates) = parseRulesAndUpdates(input)
        var middleSum = 0
        updates.forEach { update ->
            val relatedRules = rules.filter { it.left in update && it.right in update }
            for (rule in relatedRules) {
                if (update.indexOf(rule.left) > update.indexOf(rule.right)) {
                    return@forEach
                }
            }
            middleSum += update[update.size / 2]
        }
        return middleSum
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = parseRulesAndUpdates(input)

        fun sortUpdate(update: List<Int>): List<Int> {
            val sorted = update.toMutableList()
            while (true) {
                var swapped = false
                sorted.indices.windowed(2).forEach { (i0, i1) ->
                    val needSwap = rules.any { it.left == sorted[i1] && it.right == sorted[i0] }
                    if (needSwap) {
                        sorted[i0] = sorted[i1].also { sorted[i1] = sorted[i0] }
                        swapped = true
                    }
                }
                if (!swapped) break
            }
            return sorted
        }

        val incorrectUpdates = updates.filter { update ->
            val relatedRules = rules.filter { it.left in update && it.right in update }
            for (rule in relatedRules) {
                if (update.indexOf(rule.left) > update.indexOf(rule.right)) {
                    return@filter true
                }
            }
            return@filter false
        }

        var middleSum = 0
        incorrectUpdates.forEach { incorrectUpdate ->
            val correctUpdate = sortUpdate(incorrectUpdate)
            middleSum += correctUpdate[correctUpdate.size / 2]
        }

        return middleSum
    }

    val testInput = readInput("05t")
    val input = readInput("05")

    check(part1(testInput) == 143)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 123)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
