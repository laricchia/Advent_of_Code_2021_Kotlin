fun firstPart14(list : List<Pair<String, String>>, start : String) {
    var newSeq = start
    val steps = 10

    for (i in 0 until steps) {
        var windows = newSeq.windowed(2).map { StringBuilder(it) }
        list.map { insertion ->
            windows = windows.map {
                if (it.toString() == insertion.first) it.insert(1, insertion.second) else it
            }
        }

        newSeq = windows.joinToString(separator = "") { it.deleteCharAt(it.length - 1).toString() } + start.last()

        // println(newSeq)
    }

    val distinctLetters = newSeq.split("").filterNot { it.isEmpty() }.distinct()

    val counts = distinctLetters.map { letter ->
        newSeq.count { it == letter.first() }
    }.sortedDescending()

    println(counts.first() - counts.last())
}

fun secondPart14(list : List<Pair<String, String>>, start : String) {
    val steps = 40
    var state = mutableMapOf<String, Long>()
    val startWindows = start.windowed(2)
    startWindows.distinct().map { s -> state[s] = startWindows.count { s == it }.toLong() }
    list.filterNot { it.first in startWindows }.map { state[it.first] = 0 }

    // println(state)

    for (i in 0 until steps) {
        val updatedState = mutableMapOf<String, Long>()

        list.map {
            val elem1 = it.first.first() + it.second
            val elem2 = it.second + it.first.last()

            val value = state[it.first] ?: 0

            updatedState[elem1] = (updatedState[elem1] ?: 0) + value
            updatedState[elem2] = (updatedState[elem2] ?: 0) + value
        }

        state = updatedState

        // println(state)
    }

    val distinctLetters = state.map { it.key }.map { it.split("").filterNot { it.isEmpty() }.map { it.first() } }.flatten().distinct()

    val counts = mutableMapOf<Char, Long>()
    distinctLetters.map { counts[it] = 0 }

    state.map {
        counts[it.key.first()] = counts[it.key.first()]!! + it.value
        counts[it.key.last()] = counts[it.key.last()]!! + it.value
    }

    //println(counts)

    val sortedCounts = counts.map {
        val updatedValue = it.value / 2
        if (it.key == start.last()) (updatedValue + 1) else updatedValue
    }.sortedDescending()

    // println(sortedCounts)

    println(sortedCounts.first() - sortedCounts.last())
}

fun main() {

    val input : List<String> = readInput("Day14")

    val start = input.first()
    val pairs = input.subList(2, input.size).map {
        val splits = it.split(" -> ")
        splits[0] to splits[1]
    }

    firstPart14(pairs, start)
    secondPart14(pairs, start)
}
