package com.advent

import com.advent.util.ResourceReaderUtil
import kotlin.math.abs


fun main() {
    val start = System.currentTimeMillis()

    println(calculateDayOnePartOne("1.txt"))
    println(calculateDayOnePartTwo("1.txt"))

    println("Total execution time is ${System.currentTimeMillis() - start} ms.")
}


fun calculateDayOnePartOne(fileName: String): Int {
    return ResourceReaderUtil.readResourceText(fileName)
        .let { input ->
            val regex = Regex("([RL])(\\d+)")
            regex.findAll(input)
                .map { match ->
                    if (match.groupValues[1] == "R")
                        match.groupValues[2].toInt()
                    else (match.groupValues[2].toInt() * -1)
                }
        }
        .toList()
        .fold(50 to 0) { acc, current ->
            val position = Math.floorMod(acc.first + current, 100)
            position to if (position == 0) acc.second + 1 else acc.second
        }.second

}

fun calculateDayOnePartTwo(fileName: String): Int {
    // TODO: fix
    return ResourceReaderUtil.readResourceText(fileName)
        .let { input ->
            val regex = Regex("([RL])(\\d+)")
            regex.findAll(input)
                .map { match ->
                    if (match.groupValues[1] == "R")
                        match.groupValues[2].toInt()
                    else (match.groupValues[2].toInt() * -1)
                }
        }
        .toList()
        .fold(50 to 0) { acc, current ->
            val position = Math.floorMod(acc.first + current, 100)
            val turns = numberOfTurns(acc.first, current)
            position to (acc.second + turns)
        }.second
}

private fun numberOfTurns(position: Int, current: Int): Int =
    current.takeIf { curr -> curr >= 0 }
        ?.let { (position + current) / 100 }
        ?: ((abs(current) + Math.floorMod(-position, 100)) / 100)
    //(abs(current) + (100 - position) % 100) / 100


//6835
//6829
//6717
//6742
// 6858

/**
 * Notes
 *
 * Math.floorMod is used to handle negative numbers correctly when calculating the new position on the circular track.
 * floorMod(x, y) = x - floor(x/y) * y
 * where floor(x/y) is the number of full turns in the circle when there are x steps to take in a total of y steps in a full circle.
 */