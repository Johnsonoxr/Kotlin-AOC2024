import kotlin.system.measureTimeMillis

fun main() {

    data class Xy(var x: Int, var y: Int)
    data class Robot(val p: Xy, val v: Xy)

    fun parseRobots(input: List<String>): List<Robot> {
        return input.map { line ->
            val (px, py, vx, vy) = Regex("[-\\d]+").findAll(line).map { it.value.toInt() }.toList()
            return@map Robot(Xy(px, py), Xy(vx, vy))
        }
    }

    fun part1(input: List<String>, bathroomSize: Xy): Int {
        val robots = parseRobots(input)
        val bx = bathroomSize.x
        val by = bathroomSize.y

        val nextRobots = robots.map { robot ->
            val px = (((robot.p.x + robot.v.x * 100) % bx) + bx) % bx
            val py = (((robot.p.y + robot.v.y * 100) % by) + by) % by
            Robot(Xy(px, py), robot.v)
        }

        val region1 = nextRobots.count { it.p.x < bathroomSize.x / 2 && it.p.y < bathroomSize.y / 2 }
        val region2 = nextRobots.count { it.p.x > bathroomSize.x / 2 && it.p.y < bathroomSize.y / 2 }
        val region3 = nextRobots.count { it.p.x > bathroomSize.x / 2 && it.p.y > bathroomSize.y / 2 }
        val region4 = nextRobots.count { it.p.x < bathroomSize.x / 2 && it.p.y > bathroomSize.y / 2 }

        return region1 * region2 * region3 * region4
    }

    fun part2(input: List<String>, bathroomSize: Xy): Int {
        val w = bathroomSize.x
        val h = bathroomSize.y
        val robots = parseRobots(input)

        var iter = 0
        while (true) {
            val offset = when {
                iter < 55 -> 1
                else -> 101
            }
            iter += offset
            "iter $iter".println()

            val graph = List(bathroomSize.y) { MutableList(bathroomSize.x) { ' ' } }
            robots.forEach { robot ->
                robot.p.x += robot.v.x * offset
                robot.p.y += robot.v.y * offset
                robot.p.x %= w
                robot.p.y %= h
                when {
                    robot.p.x >= w -> robot.p.x -= w
                    robot.p.x < 0 -> robot.p.x += w
                }
                when {
                    robot.p.y >= h -> robot.p.y -= h
                    robot.p.y < 0 -> robot.p.y += h
                }
                graph[robot.p.y][robot.p.x] = '*'
            }

            graph.joinToString("\n") { it.joinToString("") }.println()

            "=".repeat(100).println()

            if (iter > 10000) break
        }

        return iter
    }

    val testInput = readInput("14t")
    val input = readInput("14")

    check(part1(testInput, Xy(11, 7)) == 12)

    val time1 = measureTimeMillis {
        part1(input, Xy(101, 103)).println()
    }

    "Part 1 completed in $time1 ms".println()

//    check(part2(testInput) == 1)

    val time2 = measureTimeMillis {
        part2(input, Xy(101, 103)).println()
    }

    "Part 2 completed in $time2 ms".println()
}
