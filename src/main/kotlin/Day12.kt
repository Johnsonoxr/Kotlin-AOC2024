import kotlin.system.measureTimeMillis

fun main() {

    data class Node<T>(val v: T, val x: Int, val y: Int)

    data class Graph<T>(val vMap: List<List<T>>, val width: Int = vMap[0].size, val height: Int = vMap.size) {
        fun getNodeIterator(): Iterator<Node<T>> = object : Iterator<Node<T>> {
            var x = 0
            var y = 0
            override fun hasNext() = y < height
            override fun next(): Node<T> {
                val node = Node(vMap[y][x], x, y)
                x++
                if (x == width) {
                    x = 0
                    y++
                }
                return node
            }
        }
    }

    fun <T> Node<T>.move(graph: Graph<T>, dx: Int = 0, dy: Int = 0): Node<T>? {
        val x = this.x + dx
        val y = this.y + dy
        return graph.vMap.getOrNull(y)?.getOrNull(x)?.let { Node(it, x, y) }
    }

    fun <T> Node<T>.neighbors(graph: Graph<T>): List<Node<T>> {
        return listOfNotNull(
            move(graph, -1, 0),
            move(graph, 0, -1),
            move(graph, 1, 0),
            move(graph, 0, 1)
        )
    }

    fun part1(input: List<String>): Int {

        val gardenGraph = Graph(input.map { line -> line.toList().map { it } })

        fun Node<Char>.goConnectedSamePlants(): Set<Node<Char>> {
            val visited: MutableSet<Node<Char>> = mutableSetOf(this)
            var uncheckedSet: Set<Node<Char>> = setOf(this)
            while (uncheckedSet.isNotEmpty()) {
                val newUncheckedSet = mutableSetOf<Node<Char>>()
                uncheckedSet.map { it.neighbors(gardenGraph) }.flatten().distinct().forEach { uncheckedNeighbor ->
                    if (uncheckedNeighbor.v == v && visited.add(uncheckedNeighbor)) {
                        newUncheckedSet += uncheckedNeighbor
                    }
                }
                uncheckedSet = newUncheckedSet
            }
            return visited
        }

        var cost = 0

        var checkedPlants = setOf<Node<Char>>()
        while (true) {
            val plant = gardenGraph.getNodeIterator().asSequence().firstOrNull { it !in checkedPlants } ?: break
            val connectedSamePlants = plant.goConnectedSamePlants()
            checkedPlants = checkedPlants + connectedSamePlants
            val area = connectedSamePlants.size
            val boundaries = connectedSamePlants.sumOf {
                val neighbors = it.neighbors(gardenGraph)
                neighbors.count { n -> n.v != it.v } + 4 - neighbors.size
            }
            cost += area * boundaries
        }

        return cost
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val testInput = readInput("12t")
    val input = readInput("12")

    check(part1(testInput) == 1930)

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
