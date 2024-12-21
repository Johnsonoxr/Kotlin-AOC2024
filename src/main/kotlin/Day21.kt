import kotlin.system.measureTimeMillis

fun main() {

    data class Position(val x: Int, val y: Int)
    data class Button(val v: Char, val p: Position)
    data class Keyboard(val buttons: List<Button>) {
        private val buttonMap = buttons.associate { it.p to it.v }
        private val positionMap = buttons.associate { it.v to it.p }

        operator fun get(v: Char): Position = positionMap[v]!!

        operator fun get(p: Position) = buttonMap[p]

        operator fun contains(p: Position): Boolean = p in buttonMap
    }

    val numericalKeyboard = Keyboard(
        listOf(
            Button('7', Position(0, 0)),
            Button('8', Position(1, 0)),
            Button('9', Position(2, 0)),
            Button('4', Position(0, 1)),
            Button('5', Position(1, 1)),
            Button('6', Position(2, 1)),
            Button('1', Position(0, 2)),
            Button('2', Position(1, 2)),
            Button('3', Position(2, 2)),
            Button('0', Position(1, 3)),
            Button('A', Position(2, 3)),
        )
    )

    val directionalKeyboard = Keyboard(
        listOf(
            Button('^', Position(1, 0)),
            Button('A', Position(2, 0)),
            Button('<', Position(0, 1)),
            Button('v', Position(1, 1)),
            Button('>', Position(2, 1)),
        )
    )

    fun part1(codes: List<String>): Int {

        class Operator(val controller: Operator?, val keyboard: Keyboard) {
            var currentP: Position = keyboard['A']
        }

        val me = Operator(null, directionalKeyboard)
        val robot3 = Operator(me, directionalKeyboard)
        val robot2 = Operator(robot3, directionalKeyboard)
        val robot1 = Operator(robot2, numericalKeyboard)

        fun Operator.press(v: Char): List<Char> {
            if (controller == null) return listOf(v)
            val target = keyboard[v]
            val horizontalMoves = when {
                currentP.x > target.x -> List(currentP.x - target.x) { '<' }
                currentP.x < target.x -> List(target.x - currentP.x) { '>' }
                else -> listOf()
            }
            val verticalMoves = when {
                currentP.y > target.y -> List(currentP.y - target.y) { '^' }
                currentP.y < target.y -> List(target.y - currentP.y) { 'v' }
                else -> listOf()
            }

            val possibleMoveCombinations = mutableSetOf<List<Char>>()
            if (currentP.copy(x = target.x) in keyboard) possibleMoveCombinations.add(horizontalMoves + verticalMoves + listOf('A'))
            if (currentP.copy(y = target.y) in keyboard) possibleMoveCombinations.add(verticalMoves + horizontalMoves + listOf('A'))

            val bestControllerMoves = possibleMoveCombinations.map { moves ->
                val cachedControllerP = controller.currentP
                val movesOfController = moves.map { controller.press(it) }.flatten()
                controller.currentP = cachedControllerP
                movesOfController
            }.minBy { it.size }

            currentP = keyboard[v]

            return bestControllerMoves
        }

        return codes.sumOf { code ->
            val inputs = code.toList().map { c ->
                robot1.press(c)
            }.flatten().joinToString("")
            inputs.length * code.take(3).toInt()
        }
    }

    fun part2(codes: List<String>): Int {

        class Operator(val controller: Operator?, val keyboard: Keyboard, var controlling: Operator? = null) {
            var currentP: Position = keyboard['A']
            val currentV: Char
                get() = keyboard[currentP]!!

            val bestCache = mutableMapOf<String, List<Char>>()

            fun getCacheKey(): String = if (controlling == null) "$currentV" else "$currentV${controlling!!.getCacheKey()}"

            fun press(v: Char): List<Char> {
                if (controller == null) return listOf(v)

                val cacheKey = "$v${getCacheKey()}"
                val moves = bestCache.getOrPut(cacheKey) {
                    val target = keyboard[v]
                    val horizontalMoves = when {
                        currentP.x > target.x -> List(currentP.x - target.x) { '<' }
                        currentP.x < target.x -> List(target.x - currentP.x) { '>' }
                        else -> listOf()
                    }
                    val verticalMoves = when {
                        currentP.y > target.y -> List(currentP.y - target.y) { '^' }
                        currentP.y < target.y -> List(target.y - currentP.y) { 'v' }
                        else -> listOf()
                    }

                    val possibleMoveCombinations = mutableSetOf<List<Char>>()
                    if (currentP.copy(x = target.x) in keyboard) possibleMoveCombinations.add(horizontalMoves + verticalMoves + listOf('A'))
                    if (currentP.copy(y = target.y) in keyboard) possibleMoveCombinations.add(verticalMoves + horizontalMoves + listOf('A'))

                    val bestControllerMoves = possibleMoveCombinations.map { moves ->
                        val cachedControllerP = controller.currentP
                        val movesOfController = moves.map { controller.press(it) }.flatten()
                        controller.currentP = cachedControllerP
                        movesOfController
                    }.minBy { it.size }

                    return@getOrPut bestControllerMoves
                }

                currentP = keyboard[v]

                return moves
            }
        }

        val me = Operator(null, directionalKeyboard)
        var controller = me
        val intermediateRobots = mutableListOf<Operator>()
        repeat(25) {
            intermediateRobots.add(Operator(controller, directionalKeyboard))
            controller.controlling = intermediateRobots.last()
            controller = intermediateRobots.last()
        }
        val lastRobot = Operator(intermediateRobots.last(), numericalKeyboard)
        controller.controlling = lastRobot

        return codes.sumOf { code ->
            val inputs = code.toList().map { c ->
                c.println()
                lastRobot.press(c)
            }.flatten().joinToString("")
            inputs.length * code.take(3).toInt()
        }
    }

    val testInput = readInput("21t")
    val input = readInput("21")

    check(part1(testInput) == 126384)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

//    check(part2(testInput) == 126384)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
