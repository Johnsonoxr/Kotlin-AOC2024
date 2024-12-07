import kotlin.system.measureTimeMillis

enum class OperationDay07 {
    ADD,
    MULTIPLY,
    OR;

    fun apply(a: Long, b: Long): Long = when (this) {
        ADD -> a + b
        MULTIPLY -> a * b
        OR -> "$a$b".toLong()
    }
}

fun main() {

    data class Equation(val value: Long, val numbers: List<Long>)

    fun parseEquations(input: List<String>): List<Equation> {
        return input.map { line ->
            val parts = line.split(": ")
            return@map Equation(parts[0].toLong(), parts[1].split(" ").map { it.toLong() })
        }
    }

    fun part1(input: List<String>): Long {
        val equations = parseEquations(input)

        val cachedPossibleOperationCombinations = mutableMapOf<Int, List<List<OperationDay07>>>()

        fun getPossibleOperationCombinations(operationCount: Int): List<List<OperationDay07>> {
            if (operationCount in cachedPossibleOperationCombinations) return cachedPossibleOperationCombinations[operationCount]!!
            if (operationCount == 0) return listOf(emptyList())
            var combinations = listOf(OperationDay07.ADD, OperationDay07.MULTIPLY).map { listOf(it) }
            repeat(operationCount - 1) {
                combinations = combinations.map { combination -> listOf(OperationDay07.ADD, OperationDay07.MULTIPLY).map { combination + it } }.flatten()
            }
            cachedPossibleOperationCombinations[operationCount] = combinations
            return combinations
        }

        var valueSum = 0L

        equations.forEach { (value, numbers) ->
            for (operations in getPossibleOperationCombinations(numbers.size - 1)) {
                var result = numbers[0]
                (numbers.drop(1).zip(operations)).forEach { (number, operation) ->
                    result = operation.apply(result, number)
                }
                if (result == value) {
                    valueSum += value
                    break
                }
            }
        }

        return valueSum
    }

    fun part2(input: List<String>): Long {
        val equations = parseEquations(input)

        val cachedPossibleOperationCombinations = mutableMapOf<Int, List<List<OperationDay07>>>()

        fun getPossibleOperationCombinations(operationCount: Int): List<List<OperationDay07>> {
            if (operationCount in cachedPossibleOperationCombinations) return cachedPossibleOperationCombinations[operationCount]!!
            if (operationCount == 0) return listOf(emptyList())
            var combinations = OperationDay07.entries.map { listOf(it) }
            repeat(operationCount - 1) {
                combinations = combinations.map { combination -> OperationDay07.entries.map { combination + it } }.flatten()
            }
            cachedPossibleOperationCombinations[operationCount] = combinations
            return combinations
        }

        var valueSum = 0L

        equations.forEach { (value, numbers) ->
            for (operations in getPossibleOperationCombinations(numbers.size - 1)) {
                var result = numbers[0]
                (numbers.drop(1).zip(operations)).forEach { (number, operation) ->
                    result = operation.apply(result, number)
                }
                if (result == value) {
                    valueSum += value
                    break
                }
            }
        }

        return valueSum
    }

    val testInput = readInput("07t")
    val input = readInput("07")

    check(part1(testInput) == 3749L)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 11387L)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
