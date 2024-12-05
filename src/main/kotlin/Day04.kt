import kotlin.system.measureTimeMillis

fun main() {

    data class Node<T>(val v: T, val x: Int, val y: Int)
    data class Graph<T>(val nodes: List<Node<T>>, val width: Int, val height: Int)

    fun toGraph(input: List<String>): Graph<Char> {
        val nodes = mutableListOf<Node<Char>>()
        for ((y, line) in input.withIndex()) {
            for ((x, c) in line.withIndex()) {
                nodes.add(Node(c, x, y))
            }
        }
        return Graph(nodes, input[0].length, input.size)
    }

    fun <T> Node<T>.move(graph: Graph<T>, dx: Int = 0, dy: Int = 0): Node<T>? {
        val x = this.x + dx
        val y = this.y + dy
        return if (x in 0 until graph.width && y in 0 until graph.height) graph.nodes.first { it.x == x && it.y == y } else null
    }

    fun part1(input: List<String>): Int {
        val graph = toGraph(input)
        var count = 0

        val directions = listOf(
            listOf(0, -1),
            listOf(1, 0),
            listOf(0, 1),
            listOf(-1, 0),
            listOf(1, -1),
            listOf(1, 1),
            listOf(-1, 1),
            listOf(-1, -1)
        )

        val mas = listOf('M', 'A', 'S')

        graph.nodes.forEach outer@{ node ->
            if (node.v != 'X') return@outer
            directions.forEach inner@{ (dx, dy) ->
                var currentNode: Node<Char>? = node
                for (v in mas) {
                    currentNode = currentNode?.move(graph, dx, dy) ?: return@inner
                    if (currentNode.v != v) return@inner
                }
                count++
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val testInput = readInput("04t")
    val input = readInput("04")

    check(part1(testInput).also { it.println() } == 18)

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
