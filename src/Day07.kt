import kotlin.math.abs
import kotlin.math.roundToInt

fun firstPart07(list : List<Int>) {
    getFuelConsumptionDescGrad(list) { average: Int ->
        sumOf { abs(it - average) }
    }
}

fun secondPart07(list : List<Int>) {
    getFuelConsumptionDescGrad(list) { average ->
        sumOf {
            val steps = abs(it - average)
            (1 .. steps).sum()
        }
    }
}

fun main() {

    val input : List<String> = readInput("Day07")
    val list = input.filter { it.isNotBlank() }.map { it.split(",").filter { it.isNotBlank() }.map { it.toInt() } }

    firstPart07(list.first())
    secondPart07(list.first())
}

inline fun getFuelConsumptionDescGrad(list: List<Int>, crossinline costFun : List<Int>.(Int) -> Int) {
    val averagePosition = list.average().roundToInt()
    var localMinReached = false

    var currentPosition = averagePosition
    var lastConsumedFuel : Int = list.costFun(averagePosition)
    var minConsumedFuel = lastConsumedFuel
    var minPosition = currentPosition
    var lastIncrease = true
    var increasingDirection = true

    var iterations = 0
    currentPosition ++
    while (!localMinReached) {
        iterations++
        val currentConsumedFuel = list.costFun(currentPosition)
        if (currentConsumedFuel < minConsumedFuel) {
            minConsumedFuel = currentConsumedFuel
            minPosition = currentPosition

            increasingDirection = lastIncrease
        }

        if (currentConsumedFuel < lastConsumedFuel) {
            if (lastIncrease) currentPosition++ else currentPosition--
        } else if (currentConsumedFuel > lastConsumedFuel) {
            if (lastIncrease) currentPosition -- else currentPosition ++
            lastIncrease = !lastIncrease
        }

        if (currentPosition == minPosition && lastIncrease == increasingDirection) localMinReached = true

        lastConsumedFuel = currentConsumedFuel

        // println("consumption at $currentPosition: $currentConsumedFuel\n")
    }

    println("Solution after $iterations iteration: ------------------")
    println("consumption at $minPosition: $minConsumedFuel")
}
