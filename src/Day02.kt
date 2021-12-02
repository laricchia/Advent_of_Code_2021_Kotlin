private const val CMD_FWD = "forward"
private const val CMD_UP = "up"
private const val CMD_DWN = "down"

fun firstPart02(commands : List<Pair<String, Int>>) {

    val fwds = commands.filter { it.first == CMD_FWD }
    val ups = commands.filter { it.first == CMD_UP }
    val downs = commands.filter { it.first == CMD_DWN }

    val finalFwd = fwds.sumOf { it.second }
    val finalUp = ups.sumOf { it.second }
    val finalDown = downs.sumOf { it.second }

    val finalDepth = finalDown - finalUp

    println("${(finalFwd * finalDepth)}")
}

fun secondPart02(commands: List<Pair<String, Int>>) {
    var aim = 0
    var depth = 0
    var position = 0

    commands.map {
        when (it.first) {
            CMD_FWD -> {
                position += it.second
                depth += (it.second * aim)
            }
            CMD_UP -> {
                aim -= it.second
            }
            CMD_DWN -> {
                aim += it.second
            }
        }
    }

    println("${depth * position}")
}


fun main() {

    val input : List<String> = readInput("Day02")
    val commands = input.filter { it.isNotEmpty() }.map { it.split(" ") }.map { it[0] to it[1].toInt() }

    firstPart02(commands)
    secondPart02(commands)
}
