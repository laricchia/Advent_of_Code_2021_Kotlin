import org.jetbrains.kotlinx.multik.api.toNDArray
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

}

fun main() {

    val input : List<String> = readInput("Day09")
    val list = input.filter { it.isNotBlank() }.map { it.split("").filter { it.isNotEmpty() }.map { it.toInt() } }

    firstPart09(list)
    // secondPart09(list)
}
