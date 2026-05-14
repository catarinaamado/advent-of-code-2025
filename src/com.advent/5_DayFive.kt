package com.advent

import com.advent.util.ResourceReaderUtil

fun main() {
    calculate5Part1("5-five.txt")
    calculate5Part2("5-five.txt")
}

fun calculate5Part1(fileName: String) {
    val lines = ResourceReaderUtil.readResourceLines(fileName)

    val splitter = lines.indexOfFirst { line -> line.isEmpty() }
    val ranges = lines.take(splitter)
        .map { line ->
            line.split("-")
                .let { LongRange(it[0].toLong(), it[1].toLong()) }
        }

    val count = lines
        .takeLast(lines.size - splitter - 1)
        .map { it.toLong() }
        .fold(0) { acc, id ->
        (acc + 1).takeIf { ranges.any { range -> range.contains(id) } } ?: acc
    }
    println(count)
}


fun calculate5Part2(fileName: String) {
    val ranges = ResourceReaderUtil.readResourceLines(fileName)
        .takeWhile { line -> line.isNotEmpty() }
        .map { line ->
            line.split("-")
                .let { LongRange(it[0].toLong(), it[1].toLong()) }
        }
        .sortedWith(compareBy({ it.first }, { it.last }))

    val prevFirst = ranges[0].first()
    val prevLast = ranges[0].last()

    val count = ranges
        .drop(1)
        .fold(Pair(prevFirst to prevLast, listOf(LongRange(prevFirst, prevLast)))) { prevAcc, range ->
            if (range.last < prevAcc.first.second) return@fold prevAcc
            Pair(
                prevAcc.first.first to range.last,
                listOf(*prevAcc.second.dropLast(1).toTypedArray(), LongRange(prevAcc.first.first, range.last))
            )
                .takeIf { (range.first <= prevAcc.first.second) }
                ?: Pair(
                    range.first to range.last,
                    listOf(*prevAcc.second.toTypedArray(), LongRange(range.first, range.last))
                )

        }.second

    println(count.sumOf { it.last - it.first + 1 })


    //println(count.size)
    // 58031933178423 too low
    // 345995423801866
    // 345995423801866
}