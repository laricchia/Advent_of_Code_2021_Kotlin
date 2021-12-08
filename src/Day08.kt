
/*
*  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg

*
* */


fun firstPart08(list : List<SevenSegmentData>) {
    val uniqueDigits = list.flatMap { it.output }.count { it.length in setOf(2, 3, 4, 7) }
    println(uniqueDigits)
}

fun secondPart08(list : List<SevenSegmentData>) {
    val uniqueDigitsSum = list.sumOf { decodeSegmentData(it) }
    println(uniqueDigitsSum)
}

fun main() {

    val input : List<String> = readInput("Day08")
    val list = input.filter { it.isNotBlank() }.map {
        val splits = it.split(" | ")
        SevenSegmentData(splits[0].split(" "), splits[1].split(" "))
    }

    firstPart08(list)
    secondPart08(list)
}

data class SevenSegmentData(val input : List<String>, val output : List<String>)

// TODO translate in English, sorry but now I need breakfast
fun decodeSegmentData(sevenSegmentData: SevenSegmentData) : Int {
    /*
    0 : 6 - a b c   e f g
    1 : 2 -     c     f
    2 : 5 - a   c d e   g
    3 : 5 - a   c d   f g
    4 : 4 -   b c d   f
    5 : 5 - a b   d   f g
    6 : 6 - a b   d e f g
    7 : 3 - a   c     f
    8 : 7 - a b c d e f g
    9 : 6 - a b c d   f g

    #letters    : possible values
    6           : (0, 6, 9)
    2           : (1)
    5           : (2, 3, 5)
    4           : (4)
    3           : (7)
    7           : (8)

    0 - trovo 1, 4, 7, 8 contando le lettere
    1 - per trovare il 5: al 4 sottraggo l'1, trovo quelli con 5 lettere e l'unico che contiene la sottrazione è il 5
    conosco 1, 4, 7, 8, 5 - mancano 0, 2, 3, 6, 9
    2 - per trovare lo 0: trovo quelli con 6 lettere, sottraggo le lettere del 5 (che le contiene sia il 6 che il 9) e trovo lo 0
    conosco 1, 4, 7, 8, 5, 0 - mancano 2, 3, 6, 9
    3 - trovo la a: sottraggo al 7 l'1 e conosco la lettera a (che è comune a molti)
    4 - trovo la lettera e, g e il 2 e il 3: tolta lettera a (punto 3) a numeri(2,3), tolto 1 (noto), tolgo 4 (noto), rimane il 3 con 1 lettera (g), e il 2 con 2 lettere (e, g)
    conosco 0, 1, 2, 3, 4, 5, 7, 8 - mancano 6, 9 (lettere a, e, g)
    5 - trovo 6 e 9: tolgo uguali - tolgo 1 - rimane 6 con 1 lettera e 9 con 0 lettere
    conosco 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    */

    val signals = mutableMapOf<Int, Set<String>>()
    val input = sevenSegmentData.input

    // STEP 0
    signals[1] = input.first { it.length == 2 }.split("").filter { it.isNotEmpty() }.toSet()
    signals[4] = input.first { it.length == 4 }.split("").filter { it.isNotEmpty() }.toSet()
    signals[7] = input.first { it.length == 3 }.split("").filter { it.isNotEmpty() }.toSet()
    signals[8] = input.first { it.length == 7 }.split("").filter { it.isNotEmpty() }.toSet()

    // STEP 1
    val fiveLetters = input.filter { it.length == 5 }.map { it.split("").filter { it.isNotEmpty() }.toSet() }
    signals[5] = fiveLetters.first { it.containsAll( (signals[4]!! - signals[1]!!)  ) }

    // STEP 2
    val sixLetters = input.filter { it.length == 6 }.map { it.split("").filter { it.isNotEmpty() }.toSet() }
    signals[0] = sixLetters.first { !it.containsAll(signals[5]!!) }

    // STEP 3
    val wireA = (signals[7]!! - signals[1]!!)

    // STEP 4
    // 4 - trovo la lettera e, g e il 2 e il 3: tolta lettera a (punto 3) a numeri(2,3),tolgo 4 (noto), rimane il 3 con 1 lettera (g), e il 2 con 2 lettere (e, g)
    //    conosco 0, 1, 2, 3, 4, 5, 7, 8 - mancano 6, 9 (lettere a, e, g)
    val signalsTwoAndThree = fiveLetters.filterNot { it.containsAll(signals[5]!!) }
    signals[3] = signalsTwoAndThree.first { it.toMutableSet().apply { removeAll(signals[4]!! + wireA) }.toSet().size == 1 }
    signals[2] = signalsTwoAndThree.first { it.toMutableSet().apply { removeAll(signals[4]!! + wireA) }.toSet().size == 2 }

    // STEP 5
    // 5 - trovo 6 e 9: tolgo uguali - tolgo 1 - rimane 6 con 1 lettera e 9 con 0 lettere
    val signalsSixAndNine = sixLetters.filterNot { it.containsAll(signals[0]!!) }
    val commonElements = signalsSixAndNine[0].intersect(signalsSixAndNine[1])
    signals[9] = signalsSixAndNine.first { it.toMutableSet().apply { removeAll(signals[1]!! + commonElements) }.size == 0 }
    signals[6] = signalsSixAndNine.first { it.toMutableSet().apply { removeAll(signals[1]!! + commonElements) }.size == 1 }

    // println(signals.toSortedMap())

    val invertedSignals = signals.map { it.value to it.key }.toMap()

    // println(invertedSignals)

    val digits = sevenSegmentData.output.map { it.split("").filter { it.isNotEmpty() }.toSet() }.map { invertedSignals[it] }

    // println(digits)
    return digits.joinToString(separator = "").toInt()
}
