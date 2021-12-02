fun firstPart(measurements : List<Int>) {
    var count = 0

    if (measurements.isNotEmpty()) {
        var currItem = measurements.first().toInt()
        measurements.subList(1, measurements.size).map {
            if (it > currItem) count++
            currItem = it
        }
    }

    println("Increases = $count")
}

fun secondPart(measurements: List<Int>) {
    val windows = measurements.windowed(3)
    val windowsSums = windows.map { it.reduce { acc, i -> acc + i } }
    firstPart(windowsSums)
}

fun main() {

    /*
    * count the number of times a depth measurement increases from the previous measurement.
    * (There is no measurement before the first measurement.)
    * */

    val input : List<String> = readInput("Day01")
    val measurements = input.filter { it.isNotEmpty() }.map { it.toInt() }

    firstPart(measurements)
    secondPart(measurements)

}
