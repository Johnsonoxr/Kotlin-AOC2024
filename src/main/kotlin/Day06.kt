import kotlin.system.measureTimeMillis

fun main() {

    data class Node<T>(val v: T, val x: Int, val y: Int)

    data class Graph<T>(val vMap: List<MutableList<T>>, val width: Int, val height: Int) {
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

        fun setNodeAt(x: Int, y: Int, value: T) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return
            }
            vMap[y][x] = value
        }
    }

    fun <T> Node<T>.move(graph: Graph<T>, dx: Int = 0, dy: Int = 0): Node<T>? {
        val x = this.x + dx
        val y = this.y + dy
        return graph.vMap.getOrNull(y)?.getOrNull(x)?.let { Node(it, x, y) }
    }

    fun <T> Node<T>.move(graph: Graph<T>, direction: Char): Node<T>? {
        return when (direction) {
            '^' -> move(graph, 0, -1)
            '>' -> move(graph, 1, 0)
            'v' -> move(graph, 0, 1)
            '<' -> move(graph, -1, 0)
            else -> throw IllegalStateException("Unexpected direction: $direction")
        }
    }

    fun toGraph(input: List<String>): Graph<Char> {
        val charMap = input.map { it.toMutableList() }
        return Graph(charMap, input[0].length, input.size)
    }

    fun getVisitedNodes(graph: Graph<Char>, startNode: Node<Char>, startDirection: Char): Set<Node<Char>> {
        var currentNode = startNode
        var currentDirection = startDirection
        val nodeVisited = mutableSetOf(currentNode)

        while (true) {
            val nextNode = currentNode.move(graph, currentDirection) ?: break
            when (nextNode.v) {
                '.' -> {
                    nodeVisited += nextNode
                    currentNode = nextNode
                }

                '#' -> {
                    currentDirection = when (currentDirection) {
                        '^' -> '>'
                        '>' -> 'v'
                        'v' -> '<'
                        '<' -> '^'
                        else -> throw IllegalStateException("Unexpected direction: $currentDirection")
                    }
                }

                else -> throw IllegalStateException("Unexpected node value: ${nextNode.v}")
            }
        }

        return nodeVisited
    }

    fun part1(input: List<String>): Int {
        val graph = toGraph(input.map { it.replace('^', '.') })
        val startNode = toGraph(input).getNodeIterator().asSequence().first { it.v == '^' }.let { Node('.', it.x, it.y) }
        return getVisitedNodes(graph, startNode, '^').size
    }

    fun part2(input: List<String>): Int {
        val graph = toGraph(input.map { it.replace('^', '.') })

        val startNode = toGraph(input).getNodeIterator().asSequence().first { it.v == '^' }.let { Node('.', it.x, it.y) }
        val visitedNodes = getVisitedNodes(graph, startNode, '^')

        data class State(val node: Node<Char>, val direction: Char)

        val validObstructions = mutableListOf<Node<Char>>()

        graph.getNodeIterator().forEach { obstructionCandidate ->
            if (obstructionCandidate.v == '#') return@forEach
            if (obstructionCandidate == startNode) return@forEach
            if (obstructionCandidate !in visitedNodes) return@forEach

            graph.setNodeAt(obstructionCandidate.x, obstructionCandidate.y, '#')

            var currentNode = startNode
            var currentDirection = '^'
            val stateVisited = mutableSetOf(State(currentNode, currentDirection))

            var loopFound = false
            while (true) {
                val nextNode = currentNode.move(graph, currentDirection) ?: break
                when (nextNode.v) {
                    '.' -> {
                        val nextState = State(nextNode, currentDirection)
                        if (nextState in stateVisited) {
                            loopFound = true
                            // Found a loop
                            break
                        }
                        stateVisited += nextState
                        currentNode = nextNode
                    }

                    '#' -> {
                        currentDirection = when (currentDirection) {
                            '^' -> '>'
                            '>' -> 'v'
                            'v' -> '<'
                            '<' -> '^'
                            else -> throw IllegalStateException("Unexpected direction: $currentDirection")
                        }
                    }

                    else -> throw IllegalStateException("Unexpected node value: ${nextNode.v}")
                }
            }

            graph.setNodeAt(obstructionCandidate.x, obstructionCandidate.y, '.')

            if (loopFound) {
                validObstructions += obstructionCandidate
            }
        }

        return validObstructions.size
    }

    val testInput = readInput("06t")
    val input = readInput("06")

    check(part1(testInput) == 41)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 6)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
