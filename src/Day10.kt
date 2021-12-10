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

}

fun main() {

    val input : List<String> = readInput("Day10")
    val list = input.filter { it.isNotBlank() }

    firstPart10(list)
    // secondPart10(list)
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
