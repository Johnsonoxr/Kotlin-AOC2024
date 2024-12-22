import kotlin.system.measureTimeMillis

fun main() {

    fun Long.prune(): Long {
        return this % 16777216
    }

    fun Long.nextSecret(): Long {
        var current = this
        current = ((current * 64) xor current).prune()
        current = ((current / 32) xor current).prune()
        current = ((current * 2048) xor current).prune()
        return current
    }

    fun part1(input: List<String>): Long {
        val initNumbers = input.map { it.toLong() }

        return initNumbers.sumOf { initNumber ->
            var nextN = initNumber
            repeat(2000) {
                nextN = nextN.nextSecret()
            }
            nextN
        }
    }

    fun part2(input: List<String>): Long {
        val initNumbers = input.map { it.toLong() }

        val bananaSumMap = mutableMapOf<String, Long>()

        initNumbers.forEach { initNumber ->
            val prices = mutableListOf(initNumber)
            repeat(2000) {
                prices.add(prices.last().nextSecret())
            }
            val bananaMap = mutableMapOf<String, Long>()
            prices.map { it % 10 }.windowed(5).forEach { (p1, p2, p3, p4, p5) ->
                val h1 = p2 - p1
                val h2 = p3 - p2
                val h3 = p4 - p3
                val h4 = p5 - p4
                val key = "$h1,$h2,$h3,$h4"
                if (key !in bananaMap) {
                    bananaMap[key] = p5
                }
            }

            bananaMap.forEach { (hash, banana) ->
                bananaSumMap[hash] = bananaSumMap.getOrDefault(hash, 0) + banana
            }
        }

        return bananaSumMap.values.max()
    }

    val input = readInput("22")

    check(part1(readInput("22t")) == 37327623L)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(readInput("22t2")) == 23L)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
