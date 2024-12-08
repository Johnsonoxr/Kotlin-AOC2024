import kotlin.system.measureTimeMillis

fun main() {

    data class Node<T>(val v: T, val x: Int, val y: Int)
    data class Position(val x: Int, val y: Int)

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

    fun <T> List<T>.getPairIterator(): Iterator<Pair<T, T>> = object : Iterator<Pair<T, T>> {

        private var idx1 = 0
        private var idx2 = 1

        override fun hasNext(): Boolean {
            return idx1 < size - 1
        }

        override fun next(): Pair<T, T> {
            val tPair = get(idx1) to get(idx2)
            if (idx2 == size - 1) {
                idx1++
                idx2 = idx1 + 1
            } else {
                idx2++
            }
            return tPair
        }
    }

    fun <T> Node<T>.move(graph: Graph<T>, dx: Int = 0, dy: Int = 0): Node<T>? {
        val x = this.x + dx
        val y = this.y + dy
        return graph.vMap.getOrNull(y)?.getOrNull(x)?.let { Node(it, x, y) }
    }

    fun part1(input: List<String>): Int {
        val antennaGraph = Graph(input.map { it.toList() })
        val antennaList = antennaGraph.getNodeIterator().asSequence().filter { it.v != '.' }.toList()
        val antinodes = mutableSetOf<Node<Char>>()
        antennaList.groupBy { it.v }.forEach { (_, antennaListWithSameName) ->
            antennaListWithSameName.getPairIterator().forEach { (a1, a2) ->
                a1.move(antennaGraph, a1.x - a2.x, a1.y - a2.y)?.copy(v = '#')?.let { antinodes.add(it) }
                a2.move(antennaGraph, a2.x - a1.x, a2.y - a1.y)?.copy(v = '#')?.let { antinodes.add(it) }
            }
        }
        return antinodes.size
    }

    fun part2(input: List<String>): Int {
        val antennaGraph = Graph(input.map { it.toList() })
        val antennaList = antennaGraph.getNodeIterator().asSequence().filter { it.v != '.' }.toList()
        val antinodes = mutableSetOf<Node<Char>>()
        antennaList.groupBy { it.v }.forEach { (_, antennaListWithSameName) ->
            antinodes.addAll(antennaListWithSameName.map { it.copy(v = '#') })
            antennaListWithSameName.getPairIterator().forEach { (a1, a2) ->
                val (dx1, dy1) = a1.x - a2.x to a1.y - a2.y
                var newA = a1
                while (true) {
                    newA = newA.move(antennaGraph, dx1, dy1)?.copy(v = '#')?.also { antinodes.add(it) } ?: break
                }

                val (dx2, dy2) = -dx1 to -dy1
                newA = a2
                while (true) {
                    newA = newA.move(antennaGraph, dx2, dy2)?.copy(v = '#')?.also { antinodes.add(it) } ?: break
                }
            }
        }
        return antinodes.size
    }

    val testInput = readInput("08t")
    val input = readInput("08")

    check(part1(testInput) == 14)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 34)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
