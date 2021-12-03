import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.count
import org.jetbrains.kotlinx.multik.ndarray.operations.joinToString

fun firstPart03(lists : List<List<Int>>) {
    val data = lists.toNDArray().transpose()

    // I assume I have a 2d array

    val rowNumber = data.shape[0]
    val colNumber = data.shape[1]

    val gammaRateBits = mk.zeros<Int>(rowNumber)
    val epsilonRateBits = gammaRateBits.copy()

    for (i in 0 until rowNumber) {
        if (data[i].count { it == 1 } > (colNumber / 2)) {
            gammaRateBits[i] = 1
            epsilonRateBits[i] = 0
        } else {
            gammaRateBits[i] = 0
            epsilonRateBits[i] = 1
        }
    }

    val gammaRate = Integer.parseInt(gammaRateBits.joinToString(separator = ""), 2)
    val epsilonRate = Integer.parseInt(epsilonRateBits.joinToString(separator = ""), 2)

    println(gammaRate * epsilonRate)

}

fun secondPart03(lists : List<List<Int>>) {
    var oxGenRatingPossibleValues = lists
    var co2ScrubRatingPossibleValues = lists

    var i = 0
    while (oxGenRatingPossibleValues.size != 1) {
        val toConsider = oxGenRatingPossibleValues.toNDArray().transpose()
        val colNumber = toConsider.shape[1]

        val oneCount = toConsider[i].count { it == 1 }

        val shouldKeepOne = toConsider[i].count { it == 1 } >= (colNumber - oneCount)
        oxGenRatingPossibleValues = (if (shouldKeepOne) oxGenRatingPossibleValues.filter { it[i] == 1 } else oxGenRatingPossibleValues.filter { it[i] == 0 })
        i++
    }

    i = 0
    while (co2ScrubRatingPossibleValues.size != 1) {
        val toConsider = co2ScrubRatingPossibleValues.toNDArray().transpose()
        val colNumber = toConsider.shape[1]

        val zeroCount = toConsider[i].count { it == 0 }

        val shouldKeepZero = zeroCount <= (colNumber - zeroCount)
        co2ScrubRatingPossibleValues = (if (shouldKeepZero) co2ScrubRatingPossibleValues.filter { it[i] == 0 } else co2ScrubRatingPossibleValues.filter { it[i] == 1 })
        i++
    }

    val oxygenRating = Integer.parseInt(oxGenRatingPossibleValues.first().joinToString(separator = ""), 2)
    val co2Rating = Integer.parseInt(co2ScrubRatingPossibleValues.first().joinToString(separator = ""), 2)

    println(oxygenRating * co2Rating)
}


fun main() {

    val input : List<String> = readInput("Day03")
    val lists = input.filter { it.isNotEmpty() }.map { it.split("").filter { it.isNotEmpty() }.map { it.toInt() } }

    firstPart03(lists)
    secondPart03(lists)
}
