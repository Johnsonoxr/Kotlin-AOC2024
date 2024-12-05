import kotlin.system.measureTimeMillis

fun main() {

    data class Node<T>(val v: T, val x: Int, val y: Int)

    data class Graph<T>(val vMap: List<List<T>>, val width: Int, val height: Int) {
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

    fun toGraph(input: List<String>): Graph<Char> {
        val charMap = input.map { it.toList() }
        return Graph(charMap, input[0].length, input.size)
    }

    fun <T> Node<T>.move(graph: Graph<T>, dx: Int = 0, dy: Int = 0): Node<T>? {
        val x = this.x + dx
        val y = this.y + dy
        return graph.vMap.getOrNull(y)?.getOrNull(x)?.let { Node(it, x, y) }
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

        val masList = listOf('M', 'A', 'S')

        graph.getNodeIterator().forEach outer@{ node ->
            if (node.v != 'X') return@outer
            directions.forEach inner@{ (dx, dy) ->
                var currentNode: Node<Char>? = node
                for (v in masList) {
                    currentNode = currentNode?.move(graph, dx, dy) ?: return@inner
                    if (currentNode.v != v) return@inner
                }
                count++
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val graph = toGraph(input)
        var count = 0

        val msSet = setOf('M', 'S')

        graph.getNodeIterator().forEach { node ->
            if (node.v != 'A') return@forEach

            val node11 = node.move(graph, -1, -1) ?: return@forEach
            val node12 = node.move(graph, 1, 1) ?: return@forEach
            if (setOf(node11.v, node12.v) != msSet) return@forEach

            val node21 = node.move(graph, -1, 1) ?: return@forEach
            val node22 = node.move(graph, 1, -1) ?: return@forEach
            if (setOf(node21.v, node22.v) != msSet) return@forEach

            count++
        }

        return count
    }

    val testInput = readInput("04t")
    val input = readInput("04")

    check(part1(testInput) == 18)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 9)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
