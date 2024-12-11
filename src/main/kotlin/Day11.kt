import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Long {
        var stones = input[0].split(" ").map { it.toLong() }

        repeat(25) {
            stones = stones.map {
                val len = it.toString().length
                when {
                    it == 0L -> listOf(1L)
                    len % 2 == 1 -> listOf(it * 2024L)
                    else -> it.toString().chunked(len / 2).map { half -> half.toLong() }
                }
            }.flatten()
        }

        return stones.size.toLong()
    }

    fun part2(input: List<String>): Long {
        val stones = input[0].split(" ").map { it.toLong() }

        var stonesCount = stones.groupBy { it }.mapValues { it.value.size.toLong() }

        repeat(75) {
            val stoneNumbers = stonesCount.keys.toList()
            val nextStoneNumbersList = stoneNumbers.map {
                val len = it.toString().length
                when {
                    it == 0L -> listOf(1L)
                    len % 2 == 1 -> listOf(it * 2024L)
                    else -> it.toString().chunked(len / 2).map { half -> half.toLong() }
                }
            }
            val nextStonesCount = mutableMapOf<Long, Long>()
            stoneNumbers.zip(nextStoneNumbersList).forEach { (number, nextNumbers) ->
                nextNumbers.forEach { nextNumber ->
                    nextStonesCount.putIfAbsent(nextNumber, 0)
                    nextStonesCount[nextNumber] = nextStonesCount[nextNumber]!! + stonesCount[number]!!
                }
            }
            stonesCount = nextStonesCount
        }

        return stonesCount.values.sum()
    }

    val testInput = readInput("11t")
    val input = readInput("11")

    check(part1(testInput) == 55312L)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

//    check(part2(testInput) == 1L)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
