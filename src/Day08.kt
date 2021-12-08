fun firstPart08(list : List<SevenSegmentData>) {
    val uniqueDigits = list.flatMap { it.output }.count { it.length in setOf(2, 3, 4, 7) }
    println(uniqueDigits)
}

fun secondPart08(list : List<Int>) {

}

fun main() {

    val input : List<String> = readInput("Day08")
    val list = input.filter { it.isNotBlank() }.map {
        val splits = it.split(" | ")
        SevenSegmentData(splits[0].split(" "), splits[1].split(" "))
    }

    firstPart08(list)
    // secondPart08(list.first())
}

data class SevenSegmentData(val input : List<String>, val output : List<String>)
