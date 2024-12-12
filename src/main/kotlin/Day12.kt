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

    fun Node<Char>.getConnectedPlants(gardenGraph: Graph<Char>): Set<Node<Char>> {
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

    fun part1(input: List<String>): Int {
        val gardenGraph = Graph(input.map { line -> line.toList().map { it } })

        var cost = 0

        var checkedPlants = setOf<Node<Char>>()
        while (true) {
            val unhandledPlant = gardenGraph.getNodeIterator().asSequence().firstOrNull { it !in checkedPlants } ?: break
            val connectedPlants = unhandledPlant.getConnectedPlants(gardenGraph)
            val area = connectedPlants.size
            val boundaries = connectedPlants.sumOf { p ->
                val neighborPlants = p.neighbors(gardenGraph)
                neighborPlants.count { np -> np.v != p.v } + 4 - neighborPlants.size
            }
            cost += area * boundaries
            checkedPlants = checkedPlants + connectedPlants
        }

        return cost
    }

    fun part2(input: List<String>): Int {
        var inputSurroundedByStars = listOf("*".repeat(input[0].length)) + input + listOf("*".repeat(input[0].length))
        inputSurroundedByStars = inputSurroundedByStars.map { "*$it*" }

        val gardenGraph = Graph(inputSurroundedByStars.map { line -> line.toList().map { it } })

        data class Boundary(val sidePlant1: Node<Char>, val sidePlant2: Node<Char>) {
            fun getSortableHash(): Int {
                return if (sidePlant1.x == sidePlant2.x) {
                    if (sidePlant1.y > sidePlant2.y)
                        sidePlant1.x + 1000 * sidePlant1.y + 0
                    else
                        sidePlant1.x + 1000 * sidePlant1.y + 1000000
                } else {
                    if (sidePlant1.x > sidePlant2.x)
                        sidePlant1.y + 1000 * sidePlant1.x + 2000000
                    else
                        sidePlant1.y + 1000 * sidePlant1.x + 3000000
                }
            }
        }

        var cost = 0

        var checkedPlants = setOf<Node<Char>>()
        while (true) {
            val unhandledPlant = gardenGraph.getNodeIterator().asSequence().firstOrNull { it.v != '*' && it !in checkedPlants } ?: break
            val connectedPlants = unhandledPlant.getConnectedPlants(gardenGraph)
            val area = connectedPlants.size
            val boundaries = connectedPlants.map { p ->
                p.neighbors(gardenGraph).filter { np -> np.v != p.v }.map { np -> Boundary(np, p) }
            }.flatten().toMutableSet()

            var discontinuousBoundaryCount = 1
            boundaries.map { it.getSortableHash() }.sorted().windowed(2) { (hash1, hash2) ->
                if (hash2 - hash1 > 1) {
                    discontinuousBoundaryCount++
                }
            }
            cost += area * discontinuousBoundaryCount

            checkedPlants = checkedPlants + connectedPlants
        }

        return cost
    }

    val input = readInput("12")

    check(part1(readInput("12t")) == 1930)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(readInput("12t0")) == 80)
    check(part2(readInput("12t1")) == 436)
    check(part2(readInput("12t2")) == 236)
    check(part2(readInput("12t3")) == 368)
    check(part2(readInput("12t")) == 1206)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
