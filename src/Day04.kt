import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.ndarray.operations.toListD2

fun firstPart04(list : List<String>) {
    val extractions = list.first().split(",").map { it.toInt() }

    val tables = list.subList(1, list.size).map { it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }.chunked(5)
    val tablesAndTransposed = tables.map {
        listOf(it, it.toNDArray().transpose().toListD2())
    }.flatten()

    val currentExtraction = extractions.subList(0, 5).toMutableList()
    var winnerSum = 0
    extraction@ for (extractedNumber in extractions.subList(5, extractions.size)) {
        currentExtraction.add(extractedNumber)
        for (currentTable in tablesAndTransposed) {
            val isBingo = checkBingo(currentExtraction, currentTable)
            if (isBingo.first) {
                winnerSum = isBingo.second
                break@extraction
            }
        }
    }

    println(winnerSum * currentExtraction.last())

}

fun checkBingo(extractions : List<Int>, table : List<List<Int>>) : Pair<Boolean, Int> {
    table.map {
        if (extractions.containsAll(it)) {
            val sumOfNotExtracted = table.flatten().filter { it !in extractions }.sum()
            return true to sumOfNotExtracted
        }
    }
    return false to 0
}

fun secondPart04(list : List<String>) {
    val extractions = list.first().split(",").map { it.toInt() }

    val tables = list.subList(1, list.size).map { it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }.chunked(5)
    val tablesAndTransposed = tables.map {
        it to it.toNDArray().transpose().toListD2()
    }

    val currentPossibleTables = tablesAndTransposed.toMutableList()
    val currentExtraction = extractions.subList(0, 5).toMutableList()
    var winnerSum = 0
    var lastWinnerExtractedNumber = 0

    for (extractedNumber in extractions.subList(5, extractions.size)) {
        currentExtraction.add(extractedNumber)
        val toBeRemoved = mutableListOf<Pair<List<List<Int>>, List<List<Int>>>>()
        for (currentTable in currentPossibleTables) {
            val isBingoFirst = checkBingo(currentExtraction, currentTable.first)
            val isBingoSecond = checkBingo(currentExtraction, currentTable.second)

            if (isBingoFirst.first) { winnerSum = isBingoFirst.second }
            if (isBingoSecond.first) { winnerSum = isBingoSecond.second }

            if (isBingoFirst.first || isBingoSecond.first) {
                toBeRemoved.add(currentTable)
                lastWinnerExtractedNumber = currentExtraction.last()
            }
        }
        currentPossibleTables.removeAll(toBeRemoved)
    }

    println(winnerSum * lastWinnerExtractedNumber)
}


fun main() {

    val input : List<String> = readInput("Day04")
    val list = input.filter { it.isNotBlank() }

    firstPart04(list)
    secondPart04(list)
}
