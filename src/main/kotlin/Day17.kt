import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): String {
        val registers = input.take(3).map { it.drop(9).split(": ").let { s -> s[0] to s[1].toInt() } }.associate { (k, v) -> k to v }.toMutableMap()
        val programs = Regex("\\d+").findAll(input.last()).map { it.value.toInt() }.toMutableList()

        val outputs = mutableListOf<Int>()

        var cursor = 0
        while (cursor < programs.size - 1) {
            val opcode = programs[cursor]
            val literalOperand = programs[cursor + 1]
            val comboOperand = when (literalOperand) {
                in 0..3 -> literalOperand
                4 -> registers["A"]!!
                5 -> registers["B"]!!
                6 -> registers["C"]!!
                7 -> throw IllegalArgumentException("Reserved")
                else -> throw IllegalArgumentException("???")
            }

            when (opcode) {
                0 -> {  //  adv
                    val numerator = registers["A"]!!
                    val denominator = 2.0.pow(comboOperand).toInt()
                    registers["A"] = numerator / denominator
                    cursor += 2
                }

                1 -> {  //  bxl
                    registers["B"] = registers["B"]!!.xor(literalOperand)
                    cursor += 2
                }

                2 -> {  //  bst
                    registers["B"] = comboOperand % 8
                    cursor += 2
                }

                3 -> {  //  jnz
                    if (registers["A"] != 0) {
                        cursor = literalOperand
                    } else {
                        cursor += 2
                    }
                }

                4 -> {  //  bxc
                    registers["B"] = registers["B"]!!.xor(registers["C"]!!)
                    cursor += 2
                }

                5 -> {  //  out
                    outputs.add(comboOperand % 8)
                    cursor += 2
                }

                6 -> {  //  bdv
                    val numerator = registers["A"]!!
                    val denominator = 2.0.pow(comboOperand).toInt()
                    registers["B"] = numerator / denominator
                    cursor += 2
                }

                7 -> {  //  cdv
                    val numerator = registers["A"]!!
                    val denominator = 2.0.pow(comboOperand).toInt()
                    registers["C"] = numerator / denominator
                    cursor += 2
                }
            }
        }

        return outputs.joinToString(",")
    }

    fun part2(input: List<String>): Int {
        val sourceRegisters = input.take(3).map { it.drop(9).split(": ").let { s -> s[0] to s[1].toInt() } }.associate { (k, v) -> k to v }
        val programs = Regex("\\d+").findAll(input.last()).map { it.value.toInt() }.toList()

        var registerA = 0
        while (true) {
            if (registerA % 1_000_000 == 0) {
                "Test on $registerA".println()
            }
            val registers = sourceRegisters.toMutableMap()
            registers["A"] = registerA

            val outputs = mutableListOf<Int>()

            var cursor = 0
            while (cursor < programs.size - 1) {
                val opcode = programs[cursor]
                val literalOperand = programs[cursor + 1]
                val comboOperand = when (literalOperand) {
                    in 0..3 -> literalOperand
                    4 -> registers["A"]!!
                    5 -> registers["B"]!!
                    6 -> registers["C"]!!
                    7 -> throw IllegalArgumentException("Reserved")
                    else -> throw IllegalArgumentException("???")
                }

                when (opcode) {
                    0 -> {  //  adv
                        val numerator = registers["A"]!!
                        val denominator = 2.0.pow(comboOperand).toInt()
                        registers["A"] = numerator / denominator
                        cursor += 2
                    }

                    1 -> {  //  bxl
                        registers["B"] = registers["B"]!!.xor(literalOperand)
                        cursor += 2
                    }

                    2 -> {  //  bst
                        registers["B"] = comboOperand % 8
                        cursor += 2
                    }

                    3 -> {  //  jnz
                        if (registers["A"] != 0) {
                            cursor = literalOperand
                        } else {
                            cursor += 2
                        }
                    }

                    4 -> {  //  bxc
                        registers["B"] = registers["B"]!!.xor(registers["C"]!!)
                        cursor += 2
                    }

                    5 -> {  //  out
                        outputs.add(comboOperand % 8)
                        cursor += 2
                    }

                    6 -> {  //  bdv
                        val numerator = registers["A"]!!
                        val denominator = 2.0.pow(comboOperand).toInt()
                        registers["B"] = numerator / denominator
                        cursor += 2
                    }

                    7 -> {  //  cdv
                        val numerator = registers["A"]!!
                        val denominator = 2.0.pow(comboOperand).toInt()
                        registers["C"] = numerator / denominator
                        cursor += 2
                    }
                }

                if (outputs.size > programs.size || outputs.zip(programs).any { (o, p) -> o != p }) {
                    break
                }
            }

            if (outputs == programs) {
                break
            }

            registerA++
        }

        return registerA
    }

    val input = readInput("17")

    check(part1(readInput("17t")) == "4,6,3,5,6,3,5,2,1,0")

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(readInput("17t2")) == 117440)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
