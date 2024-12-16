import kotlin.system.measureTimeMillis

fun main() {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(xy: Position): Position = Position(x + xy.x, y + xy.y)
    }

    data class Node(val v: Char, val p: Position)

    data class Graph(val vMap: List<MutableList<Char>>, val width: Int = vMap[0].size, val height: Int = vMap.size) {
        fun getNodeIterator(): Iterator<Node> = object : Iterator<Node> {
            var x = 0
            var y = 0
            override fun hasNext() = y < height
            override fun next(): Node {
                val node = Node(vMap[y][x], Position(x, y))
                x++
                if (x == width) {
                    x = 0
                    y++
                }
                return node
            }
        }

        operator fun get(p: Position): Char {
            return vMap[p.y][p.x]
        }

        operator fun set(p: Position, v: Char) {
            vMap[p.y][p.x] = v
        }
    }

    val left = Position(-1, 0)
    val up = Position(0, -1)
    val right = Position(1, 0)
    val down = Position(0, 1)

    fun getPositionOffsetFromDirection(dir: Char) = when (dir) {
        '>' -> right
        'v' -> down
        '<' -> left
        '^' -> up
        else -> throw IllegalArgumentException("Illegal dir $dir")
    }

    fun part1(input: List<String>): Int {
        val graph = Graph(input.map { it.toMutableList() })
        val end = graph.getNodeIterator().asSequence().find { it.v == 'E' }!!
        var current = graph.getNodeIterator().asSequence().find { it.v == 'S' }!!

        fun forward(p: Position, dir: Char): Position {
            return p + getPositionOffsetFromDirection(dir)
        }

        return 1
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val testInput = readInput("16t")
    val input = readInput("16")

    check(part1(testInput) == 1)

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
