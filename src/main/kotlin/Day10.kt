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
        val topographicMap = Graph(input.map { line -> line.toList().map { it.toString().toInt() } })

        fun Node<Int>.findTrailHeads(): Set<Node<Int>> {
            if (v == 0) return setOf(this)
            return neighbors(graph = topographicMap)
                .filter { neighbor -> neighbor.v == v - 1 }
                .map { neighbor -> neighbor.findTrailHeads() }
                .flatten()
                .toSet()
        }

        val nineList = topographicMap.getNodeIterator().asSequence().filter { it.v == 9 }.toList()
        return nineList.sumOf { it.findTrailHeads().size }
    }

    fun part2(input: List<String>): Int {
        val topographicMap = Graph(input.map { line -> line.toList().map { it.toString().toInt() } })

        fun Node<Int>.countTrails(): Int {
            if (v == 0) return 1
            return neighbors(graph = topographicMap)
                .filter { neighbor -> neighbor.v == v - 1 }
                .sumOf { neighbor -> neighbor.countTrails() }
        }

        val nineList = topographicMap.getNodeIterator().asSequence().filter { it.v == 9 }.toList()
        return nineList.sumOf { it.countTrails() }
    }

    val testInput = readInput("10t")
    val input = readInput("10")

    check(part1(testInput) == 36)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 81)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
