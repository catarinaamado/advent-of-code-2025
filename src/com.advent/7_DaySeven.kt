package org.example.com.advent

import com.advent.util.ResourceReaderUtil
import kotlin.math.abs


fun main() {
    calculateFirstPart("7-seven-test.txt")
    calculateSecondPart("7-seven.txt")
}

private const val TACHYON = 'S'
private const val SPLITTER = '^'

fun calculateFirstPart(fileName: String): MutableList<Pair<Int, Int>> {
    // find taquion position: pair(line, column))
    // first beam position: pair(taquion-line+1, taquion-colum)
    // map all splitters to their positions (coordinates: line, column)
    // iterate beam position down the matrix (column)
    // if beam position corresponds to the same coordinates of a splitter, then
    //      replace current beam position by 2 new beam positions,
    //      where the position is given by splitter's line position and column -1 and +1.
    // and increase te counter of splitters
    //

    val beamPositions = mutableListOf<Pair<Int, Int>>()
    val count = ResourceReaderUtil.readResourceLines(fileName)
        .takeWhile { line -> line.isNotEmpty() }
        .foldIndexed(0L) { indexLine, accByLine, stringLine ->
            stringLine.toCharArray().toList().foldIndexed(0L) { indexChar, accByCol, ch ->
                if (ch == TACHYON) {
                    beamPositions.add(Pair(indexLine + 1, indexChar))
                }
                if (beamPositions.contains(Pair(indexLine - 1, indexChar))) {
                    if (ch == SPLITTER) {
                        beamPositions.add(Pair(indexLine, indexChar - 1))
                        beamPositions.add(Pair(indexLine, indexChar + 1))
                        return@foldIndexed accByCol + 1
                    }
                    beamPositions.add(Pair(indexLine, indexChar))

                }
                accByCol
            } + accByLine
        }
    println("Total Count is $count.")
    return beamPositions
}

fun calculateSecondPart(fileName: String) {

    val beamPositions = calculateFirstPart(fileName).distinct()
    val coordinatesByMatrixRow = beamPositions.groupBy { it.first }
    val minX = coordinatesByMatrixRow.keys.min()
    val maxX = coordinatesByMatrixRow.keys.max()

    val graph = mutableMapOf<Pair<Int, Int>, List<Pair<Int, Int>>>()
    val graph2 = mutableMapOf<Pair<Int, Int>, List<Pair<Int, Int>>>()

    // Build the graph: for every node, find all next nodes - favor in same-column nodes, otherwise, adjacent nodes (y diff == 1)

    /*
    IntStream.range(minX, maxX)
        .forEach { x ->
            coordinatesByMatrixRow[x].orEmpty()
                .forEach { yNode ->
                    graph[yNode] = coordinatesByMatrixRow[x + 1].orEmpty()
                        .filter { nextNode -> abs(nextNode.second - yNode.second) == 0 } //favor same y if exist
                        .takeIf { it.isNotEmpty() }
                        ?: coordinatesByMatrixRow[x + 1].orEmpty()
                            .filter { nextNode -> abs(nextNode.second - yNode.second) == 1 } // or assume adjacent
                }
        }

     */

    beamPositions
        .filter { coord -> coord.first < beamPositions.map { it.first }.max() }
        .forEach { node ->
            graph2[node] = beamPositions.filter { nextNode ->
                (nextNode.first > node.first) && (nextNode.first - node.first) == 1 &&
                        abs(nextNode.second - node.second) == 0
            }.takeIf { edges -> edges.isNotEmpty() }
                ?: beamPositions.filter { nextNode ->
                    (nextNode.first > node.first) && (nextNode.first - node.first) == 1 &&
                            abs(nextNode.second - node.second) == 1
                }
        }


    val memo = mutableMapOf<Pair<Int, Int>, Long>()
    val totalPaths = coordinatesByMatrixRow[minX].orEmpty()
        .sumOf { countPaths(it, graph2, memo, maxX) }
    println("Result yy=$totalPaths")
}

//Memoized count — countPaths computes how many paths reach maxX from a given node, caching the result per node so each node is visited only once
fun countPaths(
    node: Pair<Int, Int>,
    graph: Map<Pair<Int, Int>, List<Pair<Int, Int>>>,
    memo: MutableMap<Pair<Int, Int>, Long>,
    maxX: Int
): Long {
    if (node.first == maxX) return 1L
    memo[node]?.let { return it }
    return graph[node].orEmpty()
        .sumOf { countPaths(it, graph, memo, maxX) }
        .also { memo[node] = it }
}


// Count how many distinct paths are possible wen traversing the matrix downwards: Direct acyclic graph

/*
Build Graph: for each node, find the neighbors Map<node, List<node>>
Count Paths for each node: sum all cunted paths of successor nodes
The number of paths from each predecessor node is the sum of all adjacent/successor node's paths
Count: every end node (x = maxX) is 1 path.
 */
/*
Depth-First Search
Explore a path as deep as possible before trying alternatives.
Walking every possible downward route through a layered graph
 */

//62943905501815