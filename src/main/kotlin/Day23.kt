import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Int {
        val connections = input.map { line -> line.split("-").toList() }
        val computers = connections.flatten().toSet()
        val cmpLinkMap = computers.associateWith { mutableSetOf<String>() }

        connections.forEach { (c1, c2) ->
            cmpLinkMap[c1]?.add(c2)
            cmpLinkMap[c2]?.add(c1)
        }

        val triConnections = mutableSetOf<Set<String>>()
        computers.forEach { cmp ->
            val linkedComputers = cmpLinkMap[cmp]!!
            linkedComputers.forEach { cmp1 ->
                val linkedComputers1 = cmpLinkMap[cmp1]!!
                linkedComputers.intersect(linkedComputers1).forEach { cmp2 ->
                    triConnections.add(setOf(cmp, cmp1, cmp2))
                }
            }
        }

        return triConnections.count { it.any { computer -> computer.startsWith('t') } }
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val testInput = readInput("23t")
    val input = readInput("23")

    check(part1(testInput) == 7)

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
