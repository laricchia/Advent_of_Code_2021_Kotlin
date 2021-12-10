import kotlin.math.floor
import kotlin.text.StringBuilder

val openingChars = setOf('(', '[', '{', '<')
val closingChars = setOf(')', ']', '}', '>')

fun firstPart10(list : List<String>) {
    val errorsCount = mutableMapOf<Char, Int>()
    closingChars.map { errorsCount[it] = 0 }

    // val scopes = mutableListOf<Scope>()

   for (line in list) {
        val root = Scope('*', false, null)
        var currentScope = root
        lineCheck@ for (it in line.toCharArray()) {
            if (it in openingChars) {
                val newScope = Scope(it, false, currentScope)
                currentScope.child.add(newScope)
                currentScope = newScope
            }
            if (it in closingChars) {
                if (it == currentScope.openingChar.expectedClosing()) {
                    currentScope.isClosed = true
                    if (currentScope.parent != null) {
                        val nextSibling = currentScope.parent!!.child.filterNot { it.isClosed }.lastOrNull()
                        currentScope = nextSibling ?: currentScope.parent!!
                    } else break@lineCheck
                } else {
                    errorsCount[it] = errorsCount[it]!! + 1
                    break@lineCheck
                }
            }
        }
    }

    println(errorsCount.map { it.key.errorScore() * it.value }.sum())


}

fun secondPart10(list : List<String>) {
    val incompleteLines = mutableListOf<Scope>()
    for (line in list) {
        val root = Scope('*', false, null)
        var currentScope = root
        incompleteLines.add(root)

        lineCheck@ for (it in line.toCharArray()) {
            if (it in openingChars) {
                val newScope = Scope(it, false, currentScope)
                currentScope.child.add(newScope)
                currentScope = newScope
            }
            if (it in closingChars) {
                if (it == currentScope.openingChar.expectedClosing()) {
                    currentScope.isClosed = true
                    if (currentScope.parent != null) {
                        val nextSibling = currentScope.parent!!.child.filterNot { it.isClosed }.lastOrNull()
                        currentScope = nextSibling ?: currentScope.parent!!
                    } else break@lineCheck

                } else {
                    incompleteLines.removeLast()
                    break@lineCheck
                }
            }
        }
    }

    // println(incompleteLines.size)

    val completitionStrings = incompleteLines.map {
        val builder = StringBuilder()
        it.close(builder)
        builder.toString()
    }

    // println(completitionStrings)

    val scores = completitionStrings.map {
        it.removeSuffix("*").fold(0L) { acc, c -> (acc * 5) + c.completionScore() }
    }

    // println(scores.sorted())

    println(scores.sorted()[floor(scores.size / 2.0).toInt()])
}

fun main() {

    val input : List<String> = readInput("Day10")
    val list = input.filter { it.isNotBlank() }

    firstPart10(list)
    secondPart10(list)
}

data class Scope(
    val openingChar : Char,
    var isClosed    : Boolean,
    val parent      : Scope?,
    val child       : MutableList<Scope> = mutableListOf(),
)

fun Char.expectedClosing() : Char {
    return when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> '*'
    }
}

fun Char.errorScore() : Int {
    return when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> 0
    }
}

fun Char.completionScore() : Long {
    return when (this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> 0
    }
}

fun Scope.close(stringBuilder: StringBuilder) {
    val openChildren = child.filterNot { it.isClosed }
    openChildren.asReversed().map { it.close(stringBuilder) }
    if (!isClosed) stringBuilder.append(openingChar.expectedClosing())
}
