import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.count
import org.jetbrains.kotlinx.multik.ndarray.operations.plus

fun firstPart13(list : List<List<Int>>, folds : List<Fold>) {
    val (sizeX, sizeY) = (list.maxOf { it[0] } + 1) to (list.maxOf { it[1] } + 1)
    val sheet = mk.zeros<Int>(sizeX, sizeY)

    list.map {
        sheet[it[0], it[1]] = 1
    }

    val fold = folds.first()

    val firstFold = sheet.fold(fold)

    println(firstFold.count { it > 0 })
}

fun secondPart13(list : List<List<Int>>, folds : List<Fold>) {
    val (sizeX, sizeY) = ((folds.filter { it.axe == Axis.X }.maxOf { it.index } * 2) + 1) to ((folds.filter { it.axe == Axis.Y }.maxOf { it.index } * 2) + 1)
    val sheet = mk.zeros<Int>(sizeX, sizeY)

    list.map {
        sheet[it[0], it[1]] = 1
    }

    var currentSheet = sheet
    for (fold in folds) {
        currentSheet = currentSheet.fold(fold)
    }

    val newSheet = mk.zeros<Byte>(currentSheet.shape[0], currentSheet.shape[1])
    for (index in newSheet.multiIndices) {
        if (currentSheet[index[0], index[1]] > 0) newSheet[index[0], index[1]] = '#'.code.toByte()
    }

    val trSheep = currentSheet.transpose()
    for (i in trSheep.multiIndices) {
        print(if (trSheep[i[0], i[1]] > 0) "███" else "   ")
        if (i[1] == trSheep.shape[1] - 1) print("\n")
    }


}

fun main() {

    val input : List<String> = readInput("Day13")

    val inputDotsSize = input.indexOfFirst { it.isEmpty() }
    val inputDots = input.take(inputDotsSize).map { it.split(",").map { it.toInt() } }
    val instructions = input.takeLast(input.size - inputDotsSize).filterNot { it.isEmpty() }

    val folds = instructions.map {
        val splits = it.split("=")
        Fold(if (splits[0].contains("x")) Axis.X else Axis.Y, splits[1].toInt())
    }

    firstPart13(inputDots, folds)
    secondPart13(inputDots, folds)
}

enum class Axis { X, Y }

data class Fold(val axe : Axis, val index : Int)

fun D2Array<Int>.fold(fold : Fold) : D2Array<Int> {
    val sizeX = this.shape[0]
    val sizeY = this.shape[1]

    when (fold.axe) {
        Axis.X -> {
            val firstPart = this[0 .. fold.index, 0 .. sizeY]
            val secondPart = this[fold.index + 1 .. sizeX, 0 .. sizeY]

            val rotatedSecond = mk.zeros<Int>(secondPart.shape[0], secondPart.shape[1])

            for (i in secondPart.multiIndices) {
                rotatedSecond[secondPart.shape[0] - i[0] - 1, i[1]] = secondPart[i[0], i[1]]
            }

            return firstPart + rotatedSecond
        }
        Axis.Y -> {
            val yDim = (shape[1] - 1) / 2
            val firstPart = this[0 .. sizeX, 0 .. yDim]
            val secondPart = this[0 .. sizeX, (if (yDim == fold.index) fold.index + 1 else fold.index) .. sizeY]

            val rotatedSecond = mk.zeros<Int>(secondPart.shape[0], secondPart.shape[1])

            for (i in secondPart.multiIndices) {
                rotatedSecond[i[0], secondPart.shape[1] - 1 - i[1]] = secondPart[i[0], i[1]]
            }

            return firstPart + rotatedSecond
        }
    }
}
