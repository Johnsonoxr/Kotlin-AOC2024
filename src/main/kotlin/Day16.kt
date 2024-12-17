import kotlin.system.measureTimeMillis

fun main() {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(xy: Position): Position = Position(x + xy.x, y + xy.y)
        operator fun minus(xy: Position): Position = Position(x - xy.x, y - xy.y)
    }

    data class Node<T>(val v: T, val p: Position)

    data class Graph<T>(val vMap: List<MutableList<T>>, val width: Int = vMap[0].size, val height: Int = vMap.size) {
        fun getNodeIterator(): Iterator<Node<T>> = object : Iterator<Node<T>> {
            var x = 0
            var y = 0
            override fun hasNext() = y < height
            override fun next(): Node<T> {
                val node = Node(vMap[y][x], Position(x, y))
                x++
                if (x == width) {
                    x = 0
                    y++
                }
                return node
            }
        }

        operator fun get(p: Position): T? {
            return vMap.getOrNull(p.y)?.getOrNull(p.x)
        }

        operator fun set(p: Position, v: T) {
            vMap[p.y][p.x] = v
        }

        operator fun contains(p: Position): Boolean {
            return p.x in 0..<width && p.y in 0..<height
        }
    }

    data class Move(val p: Position, val d: Position, val score: Int)

    val dirList = listOf(
        Position(1, 0),
        Position(0, 1),
        Position(-1, 0),
        Position(0, -1)
    )

    fun part1(input: List<String>): Int {
        val graph0 = Graph(input.map { it.toMutableList() })
        val end = graph0.getNodeIterator().asSequence().find { it.v == 'E' }!!
        val start = graph0.getNodeIterator().asSequence().find { it.v == 'S' }!!

        val graph: Graph<MutableMap<Position, Int>?> =  //  map of direction to score
            Graph(graph0.vMap.map { it.map { dir -> if (dir == '#') null else mutableMapOf<Position, Int>() }.toMutableList() })

        var moves = setOf(Move(start.p, Position(1, 0), 0))

        while (moves.isNotEmpty()) {
            val nextMoves = mutableSetOf<Move>()
            moves.forEach { move ->
                val forward = move.copy(p = move.p + move.d, score = move.score + 1)
                val clockwise = move.copy(d = dirList[(dirList.indexOf(move.d) + 1) % 4], score = move.score + 1000)
                val counterClockwise = move.copy(d = dirList[(dirList.indexOf(move.d) + 3) % 4], score = move.score + 1000)
                listOf(
                    forward,
                    clockwise,
                    counterClockwise
                ).forEach nextMoveCheck@{ nextMove ->
                    val dirMap = graph[nextMove.p] ?: return@nextMoveCheck
                    if (nextMove.score >= (dirMap[nextMove.d] ?: Int.MAX_VALUE)) return@nextMoveCheck
                    dirMap[nextMove.d] = nextMove.score
                    nextMoves.add(nextMove)
                }
            }
            moves = nextMoves
        }

        return graph[end.p]!!.values.min()
    }

    fun part2(input: List<String>): Int {
        val graph0 = Graph(input.map { it.toMutableList() })
        val end = graph0.getNodeIterator().asSequence().find { it.v == 'E' }!!
        val start = graph0.getNodeIterator().asSequence().find { it.v == 'S' }!!

        val graph: Graph<MutableMap<Position, Int>?> =  //  map of direction to score
            Graph(graph0.vMap.map { it.map { dir -> if (dir == '#') null else mutableMapOf<Position, Int>() }.toMutableList() })

        var moves = setOf(Move(start.p, Position(1, 0), 0))

        while (moves.isNotEmpty()) {
            val nextMoves = mutableSetOf<Move>()
            moves.forEach { move ->
                val forward = move.copy(p = move.p + move.d, score = move.score + 1)
                val cw = move.copy(d = dirList[(dirList.indexOf(move.d) + 1) % 4], score = move.score + 1000)
                val ccw = move.copy(d = dirList[(dirList.indexOf(move.d) + 3) % 4], score = move.score + 1000)
                listOf(
                    forward,
                    cw,
                    ccw
                ).forEach nextMoveCheck@{ nextMove ->
                    val dirMap = graph[nextMove.p] ?: return@nextMoveCheck
                    if (nextMove.score >= (dirMap[nextMove.d] ?: Int.MAX_VALUE)) return@nextMoveCheck
                    dirMap[nextMove.d] = nextMove.score
                    nextMoves.add(nextMove)
                }
            }
            moves = nextMoves
        }

        val lowestScore = graph[end.p]!!.values.min()
        moves = graph[end.p]!!.filter { it.value == lowestScore }.map { Move(end.p, it.key, lowestScore) }.toSet()

        val canvasGraph = Graph(input.map { it.toMutableList() })

        while (moves.isNotEmpty()) {
            val nextMoves = mutableSetOf<Move>()
            moves.map { move ->
                canvasGraph[move.p] = 'O'

                val backward = move.copy(p = move.p - move.d, score = move.score - 1)
                val ccw = move.copy(d = dirList[(dirList.indexOf(move.d) + 1) % 4], score = move.score - 1000)
                val cw = move.copy(d = dirList[(dirList.indexOf(move.d) + 3) % 4], score = move.score - 1000)
                listOf(
                    backward,
                    ccw,
                    cw
                ).forEach nextMoveCheck@{ nextMove ->
                    val dirMap = graph[nextMove.p] ?: return@nextMoveCheck
                    if (nextMove.score != dirMap[nextMove.d]) return@nextMoveCheck
                    nextMoves.add(nextMove)
                }
            }
            moves = nextMoves
        }
        canvasGraph[start.p] = 'O'

        return canvasGraph.getNodeIterator().asSequence().count { it.v == 'O' }
    }

    val testInput = readInput("16t")
    val input = readInput("16")

    check(part1(testInput) == 11048)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 64)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
