import kotlin.system.measureTimeMillis

fun main() {

    data class Equation(val value: Long, val numbers: List<Long>)

    fun parseEquations(input: List<String>): List<Equation> {
        return input.map { line ->
            val parts = line.split(": ")
            return@map Equation(parts[0].toLong(), parts[1].split(" ").map { it.toLong() })
        }
    }

    fun checkCalibration(value: Long, numbers: List<Long>, operations: List<OperationDay07>): Boolean {
        if (numbers.size == 1) return numbers[0] == value
        return operations.any { operation ->
            val newNumbers = listOf(operation.apply(numbers[0], numbers[1])) + numbers.drop(2)
            checkCalibration(value, newNumbers, operations)
        }
    }

    fun part1(input: List<String>): Long {
        val equations = parseEquations(input)
        return equations
            .filter { (value, numbers) -> checkCalibration(value, numbers, listOf(OperationDay07.ADD, OperationDay07.MULTIPLY)) }
            .sumOf { it.value }
    }

    fun part2(input: List<String>): Long {
        val equations = parseEquations(input)
        return equations
            .filter { (value, numbers) -> checkCalibration(value, numbers, listOf(OperationDay07.ADD, OperationDay07.MULTIPLY, OperationDay07.OR)) }
            .sumOf { it.value }
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
