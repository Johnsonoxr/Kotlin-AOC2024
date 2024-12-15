import kotlin.system.measureTimeMillis

fun main() {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(xy: Position): Position = Position(x + xy.x, y + xy.y)
    }

    data class Node(val v: Char, val p: Position)

    data class Graph(val vMap: List<MutableList<Char>>, val width: Int = vMap[0].size, val height: Int = vMap.size) {
        fun getNodeIterator(): Iterator<Node> = object : Iterator<Node> {
            var x = 0
            var y = 0
            override fun hasNext() = y < height
            override fun next(): Node {
                val node = Node(vMap[y][x], Position(x, y))
                x++
                if (x == width) {
                    x = 0
                    y++
                }
                return node
            }
        }

        operator fun get(p: Position): Char {
            return vMap[p.y][p.x]
        }

        operator fun set(p: Position, v: Char) {
            vMap[p.y][p.x] = v
        }
    }

    val left = Position(-1, 0)
    val up = Position(0, -1)
    val right = Position(1, 0)
    val down = Position(0, 1)

    fun getPositionOffsetFromDirection(dir: Char) = when (dir) {
        '>' -> right
        'v' -> down
        '<' -> left
        '^' -> up
        else -> throw IllegalArgumentException("Illegal dir $dir")
    }

    fun part1(input: List<String>): Int {
        val graph: Graph = input.take(input.indexOfFirst { it.isBlank() }).map { it.toMutableList() }.let { Graph(it) }
        val moves: List<Char> = input.drop(input.indexOfFirst { it.isBlank() }).map { it.toList() }.flatten()

        var robot = graph.getNodeIterator().asSequence().find { it.v == '@' }!!

        fun push(p: Position, dir: Char): Boolean {
            val pNext = p + getPositionOffsetFromDirection(dir)
            when (graph[pNext]) {
                '.' -> {
                    graph[pNext] = graph[p]
                    graph[p] = '.'
                    return true
                }

                'O' -> if (push(pNext, dir)) {
                    graph[pNext] = graph[p]
                    graph[p] = '.'
                    return true
                } else {
                    return false
                }

                else -> return false
            }
        }

        moves.forEach { move ->
            if (push(robot.p, move)) {
                robot = robot.copy(p = robot.p + getPositionOffsetFromDirection(move))
            }
        }

        return graph.getNodeIterator().asSequence().filter { it.v == 'O' }.sumOf { it.p.y * 100 + it.p.x }
    }

    fun part2(input: List<String>): Int {
        val graph: Graph = input.take(input.indexOfFirst { it.isBlank() })
            .map {
                it.toList().joinToString("") { c ->
                    when (c) {
                        '#' -> "##"
                        'O' -> "[]"
                        '.' -> ".."
                        '@' -> "@."
                        else -> throw IllegalArgumentException("???")
                    }
                }.toMutableList()
            }
            .let { Graph(it) }
        val moves: List<Char> = input.drop(input.indexOfFirst { it.isBlank() }).map { it.toList() }.flatten()

        var robot = graph.getNodeIterator().asSequence().find { it.v == '@' }!!

        fun checkMove(p: Position, dir: Char): Boolean {
            val pNext = p + getPositionOffsetFromDirection(dir)
            return when (graph[pNext]) {
                '.' -> true
                '[' -> {
                    when (dir) {
                        '>' -> checkMove(pNext + right, dir)
                        '^', 'v' -> checkMove(pNext, dir) && checkMove(pNext + right, dir)
                        '<' -> checkMove(pNext, dir)
                        else -> throw IllegalArgumentException("Not gonna happen.")
                    }
                }

                ']' -> {
                    when (dir) {
                        '<' -> checkMove(pNext + left, dir)
                        '^', 'v' -> checkMove(pNext, dir) && checkMove(pNext + left, dir)
                        '>' -> checkMove(pNext, dir)
                        else -> throw IllegalArgumentException("Not gonna happen.")
                    }
                }

                else -> false
            }
        }

        fun push(p: Position, dir: Char) {
            val pNext = p + getPositionOffsetFromDirection(dir)
            when (graph[pNext]) {
                '[' -> when (dir) {
                    '<' -> push(pNext, dir)
                    else -> {
                        push(pNext + right, dir)
                        push(pNext, dir)
                    }
                }

                ']' -> when (dir) {
                    '>' -> push(pNext, dir)
                    else -> {
                        push(pNext + left, dir)
                        push(pNext, dir)
                    }
                }
            }
            graph[pNext] = graph[p]
            graph[p] = '.'
        }

        moves.forEach { move ->
            if (checkMove(robot.p, move)) {
                push(robot.p, move)
                robot = robot.copy(p = robot.p + getPositionOffsetFromDirection(move))
            }
        }

        graph.vMap.joinToString("\n") { it.joinToString("") }.println()

        return graph.getNodeIterator().asSequence().filter { it.v == '[' }.sumOf { it.p.y * 100 + it.p.x }
    }

    val testInput = readInput("15t")
    val input = readInput("15")

    check(part1(testInput) == 10092)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 9021)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
