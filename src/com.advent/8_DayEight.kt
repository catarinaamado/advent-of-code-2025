package org.example.com.advent

import com.advent.util.ResourceReaderUtil
import kotlin.math.pow
import kotlin.math.sqrt


fun main() {
    calculate8FirstPart("8-eight-test.txt")
    //calculate8SecondPart("8-eight.txt")
}


fun calculate8FirstPart(fileName: String) {
    val boxes = ResourceReaderUtil.readResourceLines(fileName)
        .map { line ->
            line.split(",").let { JunctionBox(it[0].toLong(), it[1].toLong(), it[2].toLong()) }
        }

    val allCircuits = mutableSetOf<Circuit>()
    boxes.forEach { a ->
        boxes.forEach { b ->
            if (a != b) allCircuits.add(Circuit(mutableSetOf(a, b), calcDistance(a, b)))
        }
    }
    val circuits = allCircuits.toList().sortedBy { it.distance }
    val shortestCircuits = mutableListOf<Circuit>()

    val shortestJunctions = circuits.take(11)
    shortestJunctions.forEach { circ ->
        addToExistingCircuitIfJunctionBoxIsAlreadyConnected(shortestCircuits, circ.junctionBoxes.first(), circ.junctionBoxes.last(), circ.distance)
    }

    shortestCircuits.sortByDescending { it.junctionBoxes.size }
    shortestCircuits.map { it.junctionBoxes.size }//.take(3)
        .forEach { c -> println(c)}
}

//6480 too low
fun addToExistingCircuitIfJunctionBoxIsAlreadyConnected(
    circuits: MutableList<Circuit>,
    a: JunctionBox,
    b: JunctionBox,
    d: Double) {
    circuits.filter { circ -> circ.junctionBoxes.contains(a)  }
        .first()
        ?.let { c ->
            val newList = c.junctionBoxes + b
            val newCircuit = c.copy(
                junctionBoxes = newList as MutableSet<JunctionBox>,
                distance = d.takeIf { d < c.distance } ?: c.distance)
            circuits.remove(c)
            circuits.add(newCircuit)}
        ?: circuits.firstOrNull { circ -> circ.junctionBoxes.contains(b) }
            ?.let { c ->
                val newList = c.junctionBoxes + a
                val newCircuit = c.copy(
                    junctionBoxes = newList as MutableSet<JunctionBox>,
                    distance = d.takeIf { d < c.distance } ?: c.distance)
                circuits.remove(c)
                circuits.add(newCircuit)}
        ?: /*circuits.takeIf { it.sumOf { circ -> circ.junctionBoxes.size } < 11 }
            ?.firstOrNull{it.distance > d}
            ?.let { circuits.remove(it)}
            */

            circuits.add(Circuit(mutableSetOf(a, b), d))

}

fun calcDistance(a: JunctionBox, b: JunctionBox): Double =
    sqrt(
        (a.x.toDouble() - b.x.toDouble()).pow(2.0) +
                (a.y.toDouble() - b.y.toDouble()).pow(2.0) +
                (a.z.toDouble() - b.z.toDouble()).pow(2.0)
    )

fun calculate8SecondPart(fileName: String) {


}

data class JunctionBox(
    val x: Long,
    val y: Long,
    val z: Long
)

data class Circuit(
    val junctionBoxes: MutableSet<JunctionBox>,
    val distance: Double
)