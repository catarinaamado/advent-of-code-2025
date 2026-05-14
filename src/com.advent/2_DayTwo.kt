package com.advent

import com.advent.util.ResourceReaderUtil

fun main() {
    println(calculateDayTwoPartOne("2-two.txt"))
    println(calculateDayTwoPartTwo("2-two.txt"))
    //println(checkValid(21010))
}

fun calculateDayTwoPartOne(fileName: String): Long {
    return ResourceReaderUtil.readResourceText(fileName)
        .let { input ->
            val regex = Regex("(\\d+)-(\\d+)")
            regex.findAll(input)
        }.toList()
        .asSequence()
        .map { matchResult -> matchResult.groupValues[1] to matchResult.groupValues[2] }
        .map { (it.first.toLong()..it.second.toLong()).toList() }
        .flatMap { it.filter { num -> num.toString().length % 2 == 0 } }.map { s -> s.toString() }
        .map { it.substring(0, it.length / 2) to it.substring(it.length / 2) }
        .filter { it.first == it.second }
        .sumOf { (it.first + it.second).toLong() }
}

fun calculateDayTwoPartTwo(fileName: String): Long {
    return ResourceReaderUtil.readResourceText(fileName)
        .let { input ->
            val regex = Regex("(\\d+)-(\\d+)")
            regex.findAll(input)
        }.toList()
        .asSequence()
        .map { matchResult -> matchResult.groupValues[1] to matchResult.groupValues[2] }
        .flatMap { (it.first.toLong()..it.second.toLong()).toList() }
        .filter { checkValid(it) }
        .sum()
}


fun checkValid(number: Long): Boolean {
    return number.toString()
        .let { input ->
            val regex = Regex("^(\\d+?)\\1+$")
            regex.findAll(input)
                .map { it.groupValues }
        }.toList().isNotEmpty()
}
