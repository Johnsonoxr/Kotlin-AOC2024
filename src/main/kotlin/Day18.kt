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

    fun part1(input: List<String>, mapSize: Int, corruptedCount: Int): Int {
        val graph = Graph(List(mapSize) { MutableList<Int?>(mapSize) { null } })
        input.map { it.split(",").let { s -> Position(s[0].toInt(), s[1].toInt()) } }.forEachIndexed { step, p ->
            graph[p] = step
        }

        data class StepPosition(val step: Int, val position: Position)

        val stepGraph = Graph(List(mapSize) { MutableList(mapSize) { Int.MAX_VALUE } })

        val startP = Position(0, 0)
        val endP = Position(mapSize - 1, mapSize - 1)
        stepGraph[startP] = 0

        var uncheckedSPs = setOf(StepPosition(0, startP))
        while (uncheckedSPs.isNotEmpty()) {
            val nextSPs = mutableSetOf<StepPosition>()
            uncheckedSPs.forEach { sp ->
                val nextSpStep = sp.step + 1
                val neighborPs = dirList.map { sp.position + it }.filter { graph[it] == null || graph[it]!! >= corruptedCount }
                neighborPs.forEach { neighborP ->
                    if (stepGraph[neighborP]?.let { it >= nextSpStep } == true) {
                        stepGraph[neighborP] = nextSpStep
                        nextSPs += StepPosition(nextSpStep, neighborP)
                    }
                }
            }
            uncheckedSPs = nextSPs
        }

        return stepGraph[endP]!!
    }

    fun part2(input: List<String>, mapSize: Int): String {
        val graph = Graph(List(mapSize) { MutableList<Int?>(mapSize) { null } })
        val corruptedPositions = input.map { it.split(",").let { s -> Position(s[0].toInt(), s[1].toInt()) } }
        corruptedPositions.forEachIndexed { step, p ->
            graph[p] = step
        }

        data class StepPosition(val step: Int, val position: Position)

        val startP = Position(0, 0)
        val endP = Position(mapSize - 1, mapSize - 1)

        corruptedPositions.indices.forEach { corruptedCount ->
            val stepGraph = Graph(List(mapSize) { MutableList(mapSize) { Int.MAX_VALUE } })
            stepGraph[startP] = 0

            var uncheckedSPs = setOf(StepPosition(0, startP))
            while (uncheckedSPs.isNotEmpty()) {
                val nextSPs = mutableSetOf<StepPosition>()
                uncheckedSPs.forEach { sp ->
                    val nextSpStep = sp.step + 1
                    val neighborPs = dirList.map { sp.position + it }.filter { graph[it] == null || graph[it]!! > corruptedCount }
                    neighborPs.forEach { neighborP ->
                        if (stepGraph[neighborP]?.let { it >= nextSpStep } == true) {
                            stepGraph[neighborP] = nextSpStep
                            nextSPs += StepPosition(nextSpStep, neighborP)
                        }
                    }
                }
                uncheckedSPs = nextSPs
            }
            if (stepGraph[endP] == Int.MAX_VALUE) {
                return corruptedPositions[corruptedCount].let { "${it.x},${it.y}" }
            }
        }
        throw IllegalStateException("Cannot reach here.")
    }

    val testInput = readInput("18t")
    val input = readInput("18")

    check(part1(testInput, 7, 12) == 22)

    val time1 = measureTimeMillis {
        part1(input, 71, 1024).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput, 7) == "6,1")

    val time2 = measureTimeMillis {
        part2(input, 71).println()
    }

    "Part 2 completed in $time2 ms".println()
}
