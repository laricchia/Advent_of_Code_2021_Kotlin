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

fun secondPart06(list : List<LanternFish>) {
    val totalDays = 256

    var fishMap = mutableMapOf<Int, Long>()
    for (i in 0 until 9) fishMap[i] = list.count { it.daysToNextBirth == i }.toLong()

    for (i in 0 until totalDays) {
        val updatedMap = fishMap.toMutableMap()
        for (key in fishMap.keys.sortedDescending()) {
            if (key > 0) {
                updatedMap[key - 1] = fishMap[key]!!
            } else {
                updatedMap[6] = fishMap[7]!! + fishMap[0]!!
                updatedMap[8] = fishMap[0]!!
            }
        }
        // println(updatedMap)
        fishMap = updatedMap
    }

    println(fishMap.values.sum())
}


fun main() {

    val input : List<String> = readInput("Day06")
    val list = input.filter { it.isNotBlank() }.map { it.split(",").filter { it.isNotBlank() }.map { LanternFish(it.toInt()) } }

    firstPart06(list.first())
    secondPart06(list.first())
}

data class LanternFish(val daysToNextBirth : Int)

fun LanternFish.decreaseNextBirth() : LanternFish = copy(daysToNextBirth = if (this.daysToNextBirth == 0) 6 else this.daysToNextBirth - 1)
