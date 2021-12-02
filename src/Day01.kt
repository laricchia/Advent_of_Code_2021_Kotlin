fun main() {

    /*
    * count the number of times a depth measurement increases from the previous measurement.
    * (There is no measurement before the first measurement.)
    * */

    val input : List<String> = readInput("Day01")

    var count = 0

    if (input.isNotEmpty()) {
        var currItem = input.first().toInt()
        input.subList(1, input.size).filter { it.isNotEmpty() }.map { it.toInt() }.map {
            if (it > currItem) count++
            currItem = it
        }
    }

    print("Increases = $count")

}
