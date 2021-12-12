import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.all
import org.jetbrains.kotlinx.multik.ndarray.operations.plus

fun firstPart11(list : List<List<Int>>) {
    val steps = 100
    var dumboMap = list.toNDArray()
    var flashCounter = 0

    for (i in 0 .. steps) {
        val setOfAdd = mutableSetOf<IntArray>()
        val setOfNeigh = mutableSetOf<IntArray>()
        while (dumboMap.find { it > 9 }.isNotEmpty()) {
            val currentNine = dumboMap.find { it > 9 }.first { it !in setOfAdd }
            setOfAdd.add(currentNine)
            val neigh = dumboMap.findNeigh(currentNine).filterNot { dumboMap[it[0], it[1]] == 0 }
            neigh.map { dumboMap[it[0], it[1]] = dumboMap[it[0], it[1]] + 1 }
            setOfNeigh.addAll(neigh)
            dumboMap[currentNine[0], currentNine[1]] = 0
            flashCounter++
        }
        // println(dumboMap)

        dumboMap = dumboMap.plus(1)

        if (steps == i) println("Flash counter at step $i: $flashCounter")
    }
}

fun secondPart11(list : List<List<Int>>) {
    var dumboMap = list.toNDArray()
    var stepCounter = 0

    while (true) {
        val setOfAdd = mutableSetOf<IntArray>()
        val setOfNeigh = mutableSetOf<IntArray>()
        while (dumboMap.find { it > 9 }.isNotEmpty()) {
            val currentNine = dumboMap.find { it > 9 }.first { it !in setOfAdd }
            setOfAdd.add(currentNine)
            val neigh = dumboMap.findNeigh(currentNine).filterNot { dumboMap[it[0], it[1]] == 0 }
            neigh.map { dumboMap[it[0], it[1]] = dumboMap[it[0], it[1]] + 1 }
            setOfNeigh.addAll(neigh)
            dumboMap[currentNine[0], currentNine[1]] = 0
        }
        if (dumboMap.all { it == 0 }) break

        dumboMap = dumboMap.plus(1)
        stepCounter++
    }

    println("All 0 at step $stepCounter")
}

fun main() {

    val input : List<String> = readInput("Day11")
    val list = input.filter { it.isNotBlank() }.map { it.split("").filterNot { it.isBlank() }.map { it.toInt() } }

    firstPart11(list)
    secondPart11(list)
}

fun D2Array<Int>.find(condition : (Int) -> Boolean) : List<IntArray> {
    val eightMap = mutableListOf<IntArray>()
    for (j in multiIndices) {
        if (condition(this[j[0], j[1]])) { eightMap.add(j.copyOf()) }
    }
    return eightMap.toList()
}

fun D2Array<Int>.findNeigh(seed : IntArray) : List<IntArray> {
    val neigh = mutableListOf<IntArray>()
    val x = seed[0]
    val y = seed[1]

    neigh.add(intArrayOf(x-1, y-1))
    neigh.add(intArrayOf(x-1, y))
    neigh.add(intArrayOf(x-1, y+1))
    neigh.add(intArrayOf(x, y-1))
    neigh.add(intArrayOf(x+1, y-1))
    neigh.add(intArrayOf(x+1, y+1))
    neigh.add(intArrayOf(x+1, y))
    neigh.add(intArrayOf(x, y+1))

    neigh.removeAll { it[0] < 0 || it[1] < 0 || it[0] >= this.shape[0] || it[1] >= this.shape[1] }

    return neigh.toList()
}
