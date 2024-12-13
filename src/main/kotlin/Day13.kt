import kotlin.math.atan
import kotlin.system.measureTimeMillis

fun main() {

    data class Xy(val x: Long, val y: Long)
    data class ClawMachine(val btnA: Xy, val btnB: Xy, val prize: Xy)

    fun parseClawMachines(input: List<String>): List<ClawMachine> {
        return input.chunked(4).map { lines ->
            val btnA = Regex("\\d+").findAll(lines[0]).map { it.value.toLong() }.let { Xy(it.first(), it.last()) }
            val btnB = Regex("\\d+").findAll(lines[1]).map { it.value.toLong() }.let { Xy(it.first(), it.last()) }
            val prize = Regex("\\d+").findAll(lines[2]).map { it.value.toLong() }.let { Xy(it.first(), it.last()) }
            ClawMachine(btnA, btnB, prize)
        }
    }

    fun part1(input: List<String>): Long {
        val clawMachines = parseClawMachines(input)

        var cost = 0L

        clawMachines.forEach { clawMachine ->
            var validPresses: Pair<Long, Long>? = null
            for (pressedA in 0..100L) {
                for (pressedB in 0..100L) {
                    val offset = Xy(
                        x = pressedA * clawMachine.btnA.x + pressedB * clawMachine.btnB.x,
                        y = pressedA * clawMachine.btnA.y + pressedB * clawMachine.btnB.y
                    )
                    if (offset == clawMachine.prize) {
                        validPresses = Pair(pressedA, pressedB)
                        break
                    }
                }
                if (validPresses != null) {
                    break
                }
            }
            validPresses?.let {
                cost += 3L * it.first + 1 * it.second
            }
        }

        return cost
    }

    fun part2(input: List<String>): Long {
        val clawMachines = parseClawMachines(input).map {
            it.copy(prize = Xy(it.prize.x + 10000000000000L, it.prize.y + 10000000000000L))
        }

        var cost = 0L

        clawMachines.forEach { clawMachine ->
            val slopeA = clawMachine.btnA.y.toDouble() / clawMachine.btnA.x.toDouble()
            val slopeB = clawMachine.btnB.y.toDouble() / clawMachine.btnB.x.toDouble()
            val slopePrize = clawMachine.prize.y.toDouble() / clawMachine.prize.x.toDouble()

            if (slopeA > slopePrize && slopeB > slopePrize || slopeA < slopePrize && slopeB < slopePrize) {
                return@forEach
            }

            val slopeLarge = if (slopeA > slopeB) slopeA else slopeB
            val slopeSmall = if (slopeA < slopeB) slopeA else slopeB
            val radLarge = atan(slopeLarge)
            val radSmall = atan(slopeSmall)
            val radPrize = atan(slopePrize)

            val radSmallDiffToPrize = radPrize - radSmall
            val radLargeDiffToPrize = radPrize - radLarge
            val radOther = 180 - radSmallDiffToPrize - radLargeDiffToPrize

            val edgePrizeLenSquare = clawMachine.prize.x * clawMachine.prize.x + clawMachine.prize.y * clawMachine.prize.y

            val edgeSmallSlope = edgePrizeLenSquare / (clawMachine.btnA.x * clawMachine.btnA.x + clawMachine.btnA.y * clawMachine.btnA.y)
        }

        return cost
    }

    val testInput = readInput("13t")
    val input = readInput("13")

    check(part1(testInput) == 480L)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

//    check(part2(testInput) == 1)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
