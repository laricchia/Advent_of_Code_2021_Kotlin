fun firstPart02(commands : List<Pair<String, Int>>) {

    val fwds = commands.filter { it.first == "forward" }
    val ups = commands.filter { it.first == "up" }
    val downs = commands.filter { it.first == "down" }

    val finalFwd = fwds.sumOf { it.second }
    val finalUp = ups.sumOf { it.second }
    val finalDown = downs.sumOf { it.second }

    val finalDepth = finalDown - finalUp

    println("${(finalFwd * finalDepth)}")
}

fun secondPart02(commands: List<Pair<String, Int>>) {

}


fun main() {

    val input : List<String> = readInput("Day02")
    val commands = input.filter { it.isNotEmpty() }.map { it.split(" ") }.map { it[0] to it[1].toInt() }

    firstPart02(commands)
}
