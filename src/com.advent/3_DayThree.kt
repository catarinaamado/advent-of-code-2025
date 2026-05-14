package com.advent

import com.advent.util.ResourceReaderUtil

fun main() {
    println(calculateDayThreePartOne("3-three-test.txt"))
    println(calculateDayThreePartTwo("3-three-test.txt"))
}

fun calculateDayThreePartOne(fileName: String): Long {
    return ResourceReaderUtil.readResourceLines(fileName)
        .map { line ->
            val bank = line.substring(0, line.length - 1).toCharArray()
            bank.sortDescending()
            val highestBatteryIdx = line.indexOf(bank[0])
            val bank2 =
                if (highestBatteryIdx + 1 == line.length)
                    line.substring(line.length - 1).toCharArray()
                else line.substring(highestBatteryIdx + 1).toCharArray()
            bank2.sortDescending()
            "${bank[0]}${bank2[0]}"
        }.sumOf { it.toLong() }
    //.forEach(::println)
}
//17332


fun calculateDayThreePartTwo(fileName: String): Long {
    return ResourceReaderUtil.readResourceLines(fileName)
        .map { line ->
            (1..12).fold("" to -1) { acc, current ->
                val remainingOriginal = line.substring(acc.second + 1, line.length - (12 - acc.first.length)).toCharArray()
                val remainingLine = line.substring(acc.second + 1, line.length - (12 - acc.first.length)).toCharArray()

                remainingLine.sortDescending()
                val highestDigit = remainingLine[0]
                //val highestDigitIdx = remainingOriginal.indexOf(highestDigit)


                "${acc.first}$highestDigit" to acc.second + 1 + remainingOriginal.indexOf(highestDigit)
            }

        }
        .also(::println)
        .sumOf { it.first.toLong() }

}

//3121910778619