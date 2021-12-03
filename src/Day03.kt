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

fun secondPart03() {

}


fun main() {

    val input : List<String> = readInput("Day03")
    val lists = input.filter { it.isNotEmpty() }.map { it.split("").filter { it.isNotEmpty() }.map { it.toInt() } }

    firstPart03(lists)
}
