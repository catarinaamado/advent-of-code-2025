package com.advent

import com.advent.util.ResourceReaderUtil

fun main() {
    println(calculateDayFourPartOne("4-four.txt"))
    println(calculateDayFourPartTwo("4-four.txt"))
}

fun calculateDayFourPartOne(fileName: String): Int {
    val paperMatrix = mapLinesToMatrix(fileName)

    return paperMatrix.entries
        .fold(0) { acc, entry ->
            val accountablePos = entry.value.fold(0) { acc2, position ->
                val countAdjacentPositions = countAdjacentPositions(paperMatrix, entry, position)
                (acc2 + 1).takeIf { countAdjacentPositions < 5 } ?: acc2
            }
            acc + accountablePos
        }
}

fun mapLinesToMatrix(fileName: String): Map<Int, List<Int>> {
    return ResourceReaderUtil.readResourceLines(fileName)
        .mapIndexed { index, string ->
            val positions = string.toCharArray()
                .mapIndexed { colIdx, ch -> colIdx to ch }
                .filter { it.second == '@' }
                .map { it.first }
            (index to positions)
        }.toMap()
}

fun calculateDayFourPartTwo(fileName: String) {
    val originalMatrix = mapLinesToMatrix(fileName)
    val result = findTotal(originalMatrix)
    println(result)
}

fun findTotal(matrix: Map<Int, List<Int>>): Int {
    val removedPositionsMatrix = findMatrixOfPositionsToBeRemoved(matrix)
    val countRemovedPositions = removedPositionsMatrix.values. flatten().count()
    if (countRemovedPositions > 0) {
        val newMatrix = matrix.entries
            .map { entry ->
                val positionsToBeRemoved = removedPositionsMatrix[entry.key] ?: emptyList()
                val newPositions = entry.value.filterNot { positionsToBeRemoved.contains(it) }
                (entry.key to newPositions)
            }.toMap()
        return countRemovedPositions + findTotal(newMatrix)
    }
    return countRemovedPositions
}

fun findMatrixOfPositionsToBeRemoved(paperMatrix: Map<Int, List<Int>>): Map<Int, List<Int>> {
    return paperMatrix.entries
        .fold(emptyMap<Int, List<Int>>()) { acc, entry ->
            val accountablePos = entry.value.fold(emptyList<Int>()) { acc2, position ->
                val countAdjacentPositions = countAdjacentPositions(paperMatrix, entry, position)
                listOf(position, *acc2.toTypedArray()).takeIf { countAdjacentPositions < 5 } ?: acc2
            }
            acc + (entry.key to accountablePos)
        }
}

fun countAdjacentPositions(paperMatrix: Map<Int, List<Int>>, entry: Map.Entry<Int, List<Int>>, position: Int): Int {
    return IntRange(entry.key - 1, entry.key + 1).fold(0) { acc3, rowIdx ->
        val countAdjacentPositionsInRow = IntRange(position - 1, position + 1).fold(0) { acc4, colIdx ->
            if (paperMatrix[rowIdx]?.contains(colIdx) ?: false) acc4 + 1 else acc4
        }
        acc3 + countAdjacentPositionsInRow
    }
}

