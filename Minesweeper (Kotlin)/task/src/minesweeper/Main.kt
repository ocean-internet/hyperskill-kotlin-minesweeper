package minesweeper

fun main() {
    val minefield = Minefield()

    do {
        try {
            print("How many mines do you want on the field? ")
            val numberOfMines = readln().ifBlank { throw RuntimeException(INVALID_NUMBER_OF_MINES) }.toInt()

            minefield.numberOfMines = numberOfMines
        } catch (exception: RuntimeException) {
            println("Error! ${exception.message} Try again")
        }
    } while (!minefield.isReady)

    try {
        do {
            try {
                println("\n${minefield.toStringWithMinesHidden()}")
                print("Set/unset mines marks or claim a cell as free: ")
                val input = readln()
                val command = Command.values().find { it.value == input.filter { it.isLetter() }.lowercase() }
                val coord = Coord(input.filter { !it.isLetter() }.trim())

                when (command) {
                    Command.MINE -> minefield.guess(coord)
                    Command.FREE -> minefield.explore(coord)
                    else -> throw RuntimeException("Invalid command")
                }
            } catch (exception: RuntimeException) {
                println("Error! ${exception.message} Try again")
            }
        } while (!minefield.hasGuessedAllMines || minefield.hasFoundAllFreeCells)

        println("\n$minefield")
        println("Congratulations! You found all the mines!")
    } catch (exception: GameEndedException) {
        println("\n${minefield}")
        println(exception.message)
    }
}
