import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Long {
        val numbers = input[0].toList().map { it.toString().toInt() }.toMutableList()
        if (numbers.size % 2 == 1) {
            numbers += listOf(0)
        }
        val memory = mutableListOf<Long?>()
        numbers.chunked(2).forEachIndexed { idx, (memAlloc, space) ->
            memory.addAll(List(memAlloc) { idx.toLong() } + List(space) { null })
        }

        var setterPtr = 0
        var getterPtr = memory.size - 1
        while (setterPtr != getterPtr) {
            if (memory[setterPtr] != null) {
                setterPtr++
                continue
            }
            if (memory[getterPtr] == null) {
                getterPtr--
                continue
            }
            memory[setterPtr++] = memory[getterPtr]
            memory[getterPtr--] = null
        }

        return memory.filterNotNull().mapIndexed { index, l -> index * l }.sum()
    }

    fun part2(input: List<String>): Long {
        val numbers = input[0].toList().map { it.toString().toInt() }.toMutableList()
        if (numbers.size % 2 == 1) {
            numbers += listOf(0)
        }
        val memory = mutableListOf<Long?>()
        numbers.chunked(2).forEachIndexed { idx, (memAlloc, space) ->
            memory.addAll(List(memAlloc) { idx.toLong() } + List(space) { null })
        }

        fun getBlockSize(idx: Int, forward: Boolean): Int {
            val ref = memory[idx]
            var size = 1
            while (true) {
                val nextIdx = if (forward) idx + size else idx - size
                if (memory.getOrNull(nextIdx) == ref) {
                    size++
                } else {
                    break
                }
            }
            return size
        }

        var setterPtr: Int
        var getterPtr = memory.size - 1
        var firstEmpty = memory.indexOfFirst { it == null }

        while (firstEmpty < getterPtr) {
            var emptySkipped = false
            setterPtr = firstEmpty

            if (memory[getterPtr] == null) {
                getterPtr--
                continue
            }
            val getterSpace = getBlockSize(getterPtr, false)

            while (setterPtr < getterPtr) {
                if (memory[setterPtr] != null) {
                    setterPtr++
                    if (!emptySkipped) {
                        firstEmpty = setterPtr
                    }
                    continue
                }
                val setterSpace = getBlockSize(setterPtr, true)
                if (getterSpace > setterSpace) {
                    emptySkipped = true
                    setterPtr += setterSpace
                    continue
                }
                repeat(getterSpace) {
                    memory[setterPtr++] = memory[getterPtr - it]
                    memory[getterPtr - it] = null
                }
                break
            }
            getterPtr -= getterSpace
        }
        return memory.mapIndexed { index, l -> if (l != null) index * l else 0 }.sum()
    }

    val testInput = readInput("09t")
    val input = readInput("09")

    check(part1(testInput) == 1928L)

    val time1 = measureTimeMillis {
        part1(input).println()
    }

    "Part 1 completed in $time1 ms".println()

    check(part2(testInput) == 2858L)

    val time2 = measureTimeMillis {
        part2(input).println()
    }

    "Part 2 completed in $time2 ms".println()
}
