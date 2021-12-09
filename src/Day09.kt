import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get

fun firstPart09(list : List<List<Int>>) {
    val caveHeatMap = list.toNDArray()

    val rows = caveHeatMap.shape[0]
    val cols = caveHeatMap.shape[1]

    val minPoints = mutableListOf<Int>()
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            val toConsider = caveHeatMap[i, j]
            val neighbours = mutableSetOf<Int>()
            if (i > 0) { neighbours.add(caveHeatMap[(i - 1), j]) }
            if ((i) < (rows - 1)) { neighbours.add(caveHeatMap[(i + 1), j]) }
            if (j > 0) { neighbours.add(caveHeatMap[i, (j - 1)]) }
            if ((j) < (cols - 1)) { neighbours.add(caveHeatMap[i, (j + 1)]) }

            if (toConsider < neighbours.minOf { it }) minPoints.add(toConsider)

            // println("To consider (${i+1}, ${j+1}): ${caveHeatMap[i, j]} - close points: $neighbours")
        }
    }
    // println(minPoints)

    println(minPoints.sumOf { it + 1 })

}

fun secondPart09(list : List<List<Int>>) {
    val caveHeatMap = list.toNDArray()

    val rows = caveHeatMap.shape[0]
    val cols = caveHeatMap.shape[1]

    val minPoints = mutableListOf<Pair<CaveCoordinates, Int>>()
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            val toConsider = caveHeatMap[i, j]
            val neighbours = caveHeatMap.getNeighbours(CaveCoordinates(i, j))
            if (toConsider < neighbours.minOf { it.second }) minPoints.add(CaveCoordinates(i, j) to toConsider)
        }
    }
    // println(minPoints)

    val result = minPoints.map { getBasinSize(caveHeatMap, mutableSetOf(), mutableListOf(it)) }

    println(result.map { it.size }.sortedDescending().take(3).reduce { acc, i -> acc * i })
}

fun main() {

    val input : List<String> = readInput("Day09")
    val list = input.filter { it.isNotBlank() }.map { it.split("").filter { it.isNotEmpty() }.map { it.toInt() } }

    firstPart09(list)
    secondPart09(list)
}

data class CaveCoordinates(val x : Int, val y : Int) {
    override fun toString(): String { return "(x: $y , y: $x)" }
}

fun D2Array<Int>.getNeighbours(coord : CaveCoordinates) : MutableSet<Pair<CaveCoordinates, Int>> {
    val rows = shape[0]
    val cols = shape[1]
    val i = coord.x
    val j = coord.y
    val neighbours = mutableSetOf<Pair<CaveCoordinates, Int>>()

    if (i > 0) { neighbours.add(CaveCoordinates(i - 1, j) to this[(i - 1), j]) }
    if (i < (rows - 1)) { neighbours.add(CaveCoordinates(i + 1, j) to this[(i + 1), j]) }
    if (j > 0) { neighbours.add(CaveCoordinates(i, j - 1) to this[i, (j - 1)]) }
    if (j < (cols - 1)) { neighbours.add(CaveCoordinates(i, j + 1) to this[i, (j + 1)]) }

    return neighbours
}

tailrec fun getBasinSize(
    caveMap: D2Array<Int>,
    alreadyExplored: MutableSet<CaveCoordinates> = mutableSetOf(),
    nextToCheck : MutableList<Pair<CaveCoordinates, Int>>,
) : MutableSet<CaveCoordinates> {
    val next = nextToCheck.removeFirstOrNull()
    return if (next == null) alreadyExplored
    else {
        alreadyExplored.add(next.first)
        val neighbours = caveMap.getNeighbours(next.first)
        nextToCheck.addAll(neighbours.filter { it.second < 9 }.filterNot { it.first in alreadyExplored })
        getBasinSize(caveMap, alreadyExplored, nextToCheck)
    }
}
