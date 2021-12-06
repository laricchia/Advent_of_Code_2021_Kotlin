fun firstPart06(list : List<LanternFish>) {
    val totalDays = 80
    val updatedList = list.toMutableList()
    var fishToAdd : Int
    (0 until totalDays).map {
        fishToAdd = 0
        updatedList.mapIndexed { index, fish ->
            if (fish.daysToNextBirth == 0) {
                fishToAdd++
            }
            updatedList[index] = fish.decreaseNextBirth()
        }
        (0 until fishToAdd).map { updatedList.add(LanternFish(8)) }

        // println("Updated list at day ${it + 1}: ${updatedList.map { it.daysToNextBirth }}")
    }
    println("Lanternfish number after $totalDays: ${updatedList.size}")
}

fun secondPart06(list : List<String>) {

}


fun main() {

    val input : List<String> = readInput("Day06")
    val list = input.filter { it.isNotBlank() }.map { it.split(",").filter { it.isNotBlank() }.map { LanternFish(it.toInt()) } }

    firstPart06(list.first())
}

data class LanternFish(val daysToNextBirth : Int)

fun LanternFish.decreaseNextBirth() : LanternFish = copy(daysToNextBirth = if (this.daysToNextBirth == 0) 6 else this.daysToNextBirth - 1)