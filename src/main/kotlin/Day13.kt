import com.johnsonoxr.exnumber.ExFloat
import com.johnsonoxr.exnumber.ExFloat.Companion.toExFloat
import kotlin.math.absoluteValue
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
        val clawMachines = parseClawMachines(input).map { it.copy(prize = Xy(it.prize.x + 10000000000000L, it.prize.y + 10000000000000L)) }

        var cost = 0L.toExFloat()

        clawMachines.forEach { clawMachine ->

            val ax = clawMachine.btnA.x.toExFloat()
            val ay = clawMachine.btnA.y.toExFloat()
            val bx = clawMachine.btnB.x.toExFloat()
            val by = clawMachine.btnB.y.toExFloat()
            val px = clawMachine.prize.x.toExFloat()
            val py = clawMachine.prize.y.toExFloat()

            //  1: x * ay / ax = y
            //  2: (x - px) * by / bx = (y - py)
            //
            //  x = y * ax / ay = (py + (x - px) * by / bx) * ax / ay
            //  x = (py - px * by / bx) * ax / ay + x * by / bx * ax / ay
            //  x * (1 - by / bx * ax / ay) = (py - px * by / bx) * ax / ay
            //  x = (py - px * by / bx) * ax / ay / (1 - by / bx * ax / ay)

            val x = (py - px * by / bx) * ax / ay / (1.0.toExFloat() - by * ax / bx / ay)

            val aStep = x / ax
            val bStep = (px - x) / bx
            val aStepRound = aStep.round()
            val bStepRound = bStep.round()

            if (aStepRound < 0 || bStepRound < 0) {
                return@forEach
            }

            if ((aStepRound - aStep).toDouble().absoluteValue < 1e-5 && (bStepRound - bStep).toDouble().absoluteValue < 1e-5) {
                cost += aStepRound * 3 + bStepRound
            }
        }

        return cost.toLong()
    }

    ExFloat.setGlobalStringConverter(ExFloat.StringConverter.DECIMAL_ROUNDED_TO_3)

    val testInput = readInput("13t")
    val input = readInput("13")

    check(part1(testInput) == 480L)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

//    check(part2(testInput) == 480L)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
