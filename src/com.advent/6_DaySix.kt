package org.example.com.advent

import com.advent.util.ResourceReaderUtil

fun main() {
    calculateSixPartOne("6-six.txt")
    calculateSixPartTwo("6-six.txt")
}


// 3263827
// 10227753257799
fun calculateSixPartTwo(fileName: String) {

    val sum: (List<Long>) -> Long = { a -> a.sum() }
    val mult: (List<Long>) -> Long = { a -> a.fold(1) { acc, i -> acc * i } }

    ResourceReaderUtil.readResourceLines(fileName)
        .asSequence().flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> (x to y) to ch }
        }.groupBy { (coordinate, _) -> coordinate.first }
        .asSequence()
        .map { it.value.map { it.second }.filter { it != ' ' }.joinToString("") }
        .fold(mutableListOf<MutableList<String>>()) { acc, item ->
            when {
                item.isEmpty() -> {
                    acc.add(mutableListOf())
                }
                else -> {
                    acc.lastOrNull()?.add(item) ?: acc.add(mutableListOf(item))
                }
            }
            acc
        }.filter { it.isNotEmpty() }.sumOf {
            when {
                it.any { it.contains("*") } -> {
                    mult(it.map { it.filter { it.isDigit() } }.map { it.toLong() })
                }
                else -> sum(it.map { it.filter { it.isDigit() } }.map { it.toLong() })
            }
        }
        .also { println(it) }


}


/*
    1- map homework sheet to a matrix of Long numbers (map index 0 and access to columns)
    2- map last line to Math operators
    3- traverse the matrix: we want to traverse the matrix lines doing the operation indicated by the same position in the last line
    4- accumulate the result of the whole columns, so that we can sum u to the next column

 */
fun calculateSixPartOne(fileName: String) {
    val lines = ResourceReaderUtil.readResourceLines(fileName)
        .map { line -> line.trim().split("\\s+".toRegex()) }

    val matrix = lines
        .dropLast(1)
        .mapIndexed { index, longs -> index to longs.map { it.toLong() } }
        .toMap()

    val multiplyFunc: (Long, Long) -> Long = { o1, o2 -> Math.multiplyExact(o1, o2) }
    val sumFunc: (Long, Long) -> Long = { o1, o2 -> Math.addExact(o1, o2) }
    val operators = lines.takeLast(1)
        .flatten()
        .map { str -> if (str == "*") multiplyFunc else sumFunc }

    val result = IntRange(0, operators.size - 1).fold(0L) { acc, idxColumn ->
        acc + matrix.keys.fold(0L) { accTotalInColumn, idxLine ->
            if (accTotalInColumn == 0L) return@fold (matrix[idxLine]?.get(idxColumn) ?: 0L)
            operators[idxColumn](accTotalInColumn, (matrix[idxLine]?.get(idxColumn) ?: 0L))
        }
    }
    println(result)
    // 5227286044585

}

/*
    1- Numbers do not have the same number of digits
 */
fun calculateSixPart2(fileName: String) {
    val lines = ResourceReaderUtil.readResourceLines(fileName)

    val (width, height) = lines.maxBy { it.length }.length to lines.size

    println("$width, $height")
    val paddedLines = lines.map { it.padEnd(width, ' ') }

    val multiplyFunc: (Int, Int) -> Int = { o1, o2 -> Math.multiplyExact(o1, o2) }
    val sumFunc: (Int, Int) -> Int = { o1, o2 -> Math.addExact(o1, o2) }

    val operators = paddedLines.takeLast(1)[0].toCharArray()
        .fold(emptyList<Char>()) { acc, ch ->
            listOf(*acc.toTypedArray(), ch)
                .takeIf { ch == '*' || ch == '+' }
                ?: listOf(*acc.toTypedArray(), acc[acc.size - 1])
        }
    val operations = operators
        .map { str -> if (str == '*') multiplyFunc else sumFunc }

    val matrix = paddedLines
        .dropLast(1)
        .mapIndexed { index, line -> index to (line.map { ch -> 0.takeIf { ch == ' ' } ?: ch.digitToInt() }) }
        .toMap()

    matrix.forEach { entry -> println(entry.value) }
    println(operators)

    val result = IntRange(0, (matrix[0]?.size?.minus(1)) ?: 0).fold(0L) { acc, idxColumn ->
        acc + matrix.keys.fold(0) { accTotalInColumn, idxLine ->
            if (matrix[idxLine]?.all { it == 0 } ?: false) return@fold accTotalInColumn
            val currChar = matrix[idxLine]?.get(idxColumn) ?: 0
            if (currChar == 0) {
                return@fold accTotalInColumn//.also { println("line: $idxLine; column: $idxColumn") }
            }
            if (accTotalInColumn == 0) return@fold currChar
            operations[idxColumn](accTotalInColumn, currChar)
        }
    }
    // 3263827


    println(result)
    // 5227286044585

}