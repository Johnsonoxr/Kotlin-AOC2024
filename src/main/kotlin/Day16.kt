import kotlin.system.measureTimeMillis

fun main() {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(xy: Position): Position = Position(x + xy.x, y + xy.y)
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

    val east = Position(-1, 0)
    val north = Position(0, -1)
    val west = Position(1, 0)
    val south = Position(0, 1)

    val dirCharList = listOf(
        '>',
        'v',
        '<',
        '^',
    )

    val dirList = listOf(
        west,
        south,
        east,
        north
    )

    fun getPositionOffsetFromDirection(dir: Char) = dirList[dirCharList.indexOf(dir)]

    fun _part1(input: List<String>): Int {
        val graph = Graph(input.map { it.toMutableList() })
        val end = graph.getNodeIterator().asSequence().find { it.v == 'E' }!!
        val start = graph.getNodeIterator().asSequence().find { it.v == 'S' }!!
        graph[end.p] = '.'

        fun <T> Position.findValidDirections(graph: Graph<T>): List<Char> = listOfNotNull(
            if (graph[this + west] == '.') '>' else null,
            if (graph[this + south] == '.') 'v' else null,
            if (graph[this + east] == '.') '<' else null,
            if (graph[this + north] == '.') '^' else null,
        )

        data class PositionDir(val p: Position, val d: Char)

        val bestPositionScore = mutableMapOf<PositionDir, Int>()

        fun evalScore(p: Position, dir: Char, graph: Graph<Char>, accumulatedScore: Int): Int? {
            if (p == end.p) {
                "Path found with score $accumulatedScore".println()
                return 0
            }

            val pDir = PositionDir(p, dir)
            if (bestPositionScore[pDir]?.let { it <= accumulatedScore } == true) {
                return null
            }
            bestPositionScore[pDir] = accumulatedScore

            val nextGraph = Graph(graph.vMap.map { it.toMutableList() }).also { it[p] = dir }

            val nextDirs = p.findValidDirections(nextGraph)
            if (nextDirs.isEmpty()) return null

            val scores = nextDirs.shuffled().mapNotNull { nextDir ->
                val stepScore = when (nextDir) {
                    dir -> 1
                    else -> when {
                        dir == '<' && nextDir == '>' -> 2001
                        dir == '^' && nextDir == 'v' -> 2001
                        dir == '>' && nextDir == '<' -> 2001
                        dir == 'v' && nextDir == '^' -> 2001
                        else -> 1001
                    }
                }
                evalScore(p + getPositionOffsetFromDirection(nextDir), nextDir, nextGraph, accumulatedScore + stepScore)?.let {
                    it + stepScore
                }
            }
            return scores.minOrNull()
        }

        val minScore = evalScore(start.p, '<', graph, 0)!!

        return minScore
    }

    fun part1(input: List<String>): Int {
        val graph0 = Graph(input.map { it.toMutableList() })
        val end = graph0.getNodeIterator().asSequence().find { it.v == 'E' }!!
        val start = graph0.getNodeIterator().asSequence().find { it.v == 'S' }!!

        data class Movement(val p: Position, val d: Char, val score: Int)

        val graph: Graph<MutableMap<Char, Int>?> =  //  map of direction to score
            Graph(graph0.vMap.map { it.map { dir -> if (dir == '#') null else mutableMapOf<Char, Int>() }.toMutableList() })

        var moves = listOf(Movement(start.p, '<', 0))

        while (moves.isNotEmpty()) {
            val nextMoves = mutableListOf<Movement>()
            moves.forEach { move ->
                val forward = move.copy(p = move.p + getPositionOffsetFromDirection(move.d), score = move.score + 1)
                val clockwise = move.copy(d = dirCharList[(dirCharList.indexOf(move.d) + 1) % 4], score = move.score + 1000)
                val counterClockwise = move.copy(d = dirCharList[(dirCharList.indexOf(move.d) + 3) % 4], score = move.score + 1000)
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
        return 1
    }

    val testInput = readInput("16t")
    val input = readInput("16")

    check(part1(testInput) == 11048)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 1)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
