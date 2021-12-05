import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.count

fun firstPart05(list : List<String>) {
    val points = readSantaPoints(list)
    val toConsider = points.filter { (it.first.x == it.second.x) || (it.first.y == it.second.y) }

    val maxX = maxOf(points.maxOf { it.first.x }, points.maxOf { it.second.x }) + 1
    val maxY = maxOf(points.maxOf { it.first.y }, points.maxOf { it.second.y }) + 1

    val board = mk.zeros<Int>(maxX, maxY)

    toConsider.map {
        if (it.first.x == it.second.x) {
            val fixedX = it.first.x
            for (i in if (it.second.y > it.first.y) it.first.y .. it.second.y else it.second.y .. it.first.y) {
                board[fixedX, i] = board[fixedX, i] + 1
            }
        } else {
            val fixedY = it.first.y
            for (i in if (it.second.x > it.first.x) it.first.x .. it.second.x else it.second.x .. it.first.x) {
                board[i, fixedY] = board[i, fixedY] + 1
            }
        }
    }

    // println(board.transpose())
    // println()
    println(board.asD2Array().count { it > 1 })
}

fun secondPart05(list : List<String>) {

}


fun main() {

    val input : List<String> = readInput("Day05")
    val list = input.filter { it.isNotBlank() }

    firstPart05(list)
}

data class SantaPoint(val x : Int, val y : Int)
fun List<String>.toSantaPoint() = SantaPoint(this[0].toInt(), this[1].toInt())

fun readSantaPoints(list: List<String>) : List<Pair<SantaPoint, SantaPoint>> {
    return list.map {
        it.split(" -> ").filter { it.isNotEmpty() }.let {
            it[0].split(",").filter { it.isNotBlank() }.toSantaPoint() to it[1].split(",").filter { it.isNotBlank() }.toSantaPoint()
        }
    }
}
