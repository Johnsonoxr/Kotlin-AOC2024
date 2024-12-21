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

    val dirList = listOf(
        Position(1, 0),
        Position(0, 1),
        Position(-1, 0),
        Position(0, -1)
    )

    fun Position.neighbors(graph: Graph<*>): List<Position> {
        return dirList.mapNotNull { d -> graph[this + d]?.let { this + d } }
    }

    fun part1(input: List<String>): Int {
        val raceGraph = Graph(input.map { it.toMutableList() })
        val startP = raceGraph.getNodeIterator().asSequence().first { it.v == 'S' }.p
        val endP = raceGraph.getNodeIterator().asSequence().first { it.v == 'E' }.p
        val stepGraph = Graph(input.map { line ->
            line.map { c ->
                when (c) {
                    '#' -> null
                    else -> Int.MAX_VALUE
                }
            }.toMutableList()
        })

        var step = 0
        var currentP = startP
        while (true) {
            stepGraph[currentP] = step++
            if (currentP == endP) {
                break
            }
            val nextPs = currentP.neighbors(stepGraph).filter { stepGraph[it] == Int.MAX_VALUE }
            if (nextPs.size != 1) {
                throw IllegalArgumentException("$currentP has ${nextPs.size} trace.")
            }
            currentP = nextPs.first()
        }

        val jumpOffsets = listOf(
            Position(2, 0),
            Position(0, 2),
            Position(-2, 0),
            Position(0, -2)
        )

        fun Position.jump(): List<Position> {
            return jumpOffsets.filter { stepGraph[this + it] != null }.map { this + it }
        }

        val jpCnt = mutableListOf<Int>()
        stepGraph.getNodeIterator().asSequence().filter { it.v != null }.forEach { n ->
            n.p.jump().map { stepGraph[it]!! - n.v!! - 2 }.filter { it > 0 }.forEach {
                jpCnt.add(it)
            }
        }
        jpCnt.groupBy { it }.mapValues { it.value.size }.toSortedMap().println()

        return stepGraph.getNodeIterator().asSequence().filter { it.v != null }.sumOf { n ->
            n.p.jump().count { stepGraph[it]!! - n.v!! >= 102 }
        }
    }

    fun part2(input: List<String>, atLeast: Int): Int {
        val raceGraph = Graph(input.map { it.toMutableList() })
        val startP = raceGraph.getNodeIterator().asSequence().first { it.v == 'S' }.p
        val endP = raceGraph.getNodeIterator().asSequence().first { it.v == 'E' }.p
        val stepGraph = Graph(input.map { line ->
            line.map { c ->
                when (c) {
                    '#' -> null
                    else -> Int.MAX_VALUE
                }
            }.toMutableList()
        })

        var step = 0
        var currentP = startP
        while (true) {
            stepGraph[currentP] = step++
            if (currentP == endP) {
                break
            }
            val nextPs = currentP.neighbors(stepGraph).filter { stepGraph[it] == Int.MAX_VALUE }
            if (nextPs.size != 1) {
                throw IllegalArgumentException("$currentP has ${nextPs.size} trace.")
            }
            currentP = nextPs.first()
        }

        val jpCnt = mutableListOf<Int>()

        step = 0
        currentP = startP
        while (currentP != endP) {
            val currentStep = stepGraph[currentP]!!
            (2..20).forEach { cheatStep ->
                (0..cheatStep).forEach { cheatStepX ->
                    val cheatStepY = cheatStep - cheatStepX
                    setOf(
                        Position(-cheatStepX, -cheatStepY),
                        Position(cheatStepX, -cheatStepY),
                        Position(-cheatStepX, cheatStepY),
                        Position(cheatStepX, cheatStepY),
                    ).mapNotNull { stepGraph[currentP + it] }.forEach { stepAfterCheat ->
                        if (stepAfterCheat - currentStep - cheatStep >= atLeast) {
                            jpCnt.add(stepAfterCheat - currentStep - cheatStep)
                        }
                    }
                }
            }

            step++
            currentP = currentP.neighbors(stepGraph).first { stepGraph[it] == step }
        }

        jpCnt.groupBy { it }.mapValues { it.value.size }.toSortedMap().println()

        return jpCnt.size
    }

    val testInput = readInput("20t")
    val input = readInput("20")

    check(part1(testInput) != Int.MIN_VALUE)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput, 50) == 32 + 31 + 29 + 39 + 25 + 23 + 20 + 19 + 12 + 14 + 12 + 22 + 4 + 3)

    val time2 = measureTimeMillis {
        part2(input, 100).println()
    }

    "Part 2 completed in $time2 ms".println()
}
