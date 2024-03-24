package minesweeper

const val INVALID_NUMBER_OF_MINES = "Invalid number of mines"

class Minefield(private val cols: Int = 9, private val rows: Int = 9) {

    private val grid = Grid(cols, rows)

    private val guesses = mutableListOf<Coord>()

    var numberOfMines: Int? = null
        set(value) {
            if ((value ?: 0) > grid.size) throw RuntimeException(INVALID_NUMBER_OF_MINES)

            field = value
            grid.clear()

            if (value == null) return

            val mines = mutableListOf<Coord>()

            while (mines.size < value) {
                val mine = Coord(
                    (1..cols).random(),
                    (1..rows).random()
                )
                if (mines.find { it.toString() == mine.toString() } == null) {
                    mines.add(mine)
                    grid.setCellValue(mine, Cell.MINE)
                }
            }
        }

    private val correctGuesses: Int
        get() = guesses.filter { grid.getCellValue(it) == Cell.MINE }.size

    val hasGuessedAllMines: Boolean
        get() = guesses.size == correctGuesses && guesses.size == numberOfMines

    val hasFoundAllFreeCells: Boolean
        get() = !grid.toString().contains(Cell.UNEXPLORED.value)

    val isReady: Boolean
        get() = numberOfMines != null

    override fun toString(): String {
        fun header() = " │${(1..grid.cols).joinToString("")}│"
        fun divider() = "—│${"—".repeat(grid.cols)}│"
        return listOf(
            header(),
            divider(),
            grid.grid.mapIndexed { rowIndex, row ->
                "${rowIndex + 1}│${
                    row.mapIndexed { colIndex, cell ->
                        when {
                            guesses.map { it.toString() }
                                .contains(Coord(colIndex + 1, rowIndex + 1).toString()) -> Cell.GUESS

                            else -> cell
                        }.value
                    }
                        .joinToString("")
                }│"
            }
                .joinToString("\n"),
            divider(),
        )
            .joinToString("\n")
    }

    fun toStringWithMinesHidden() = this.toString().replace(Cell.MINE.value, Cell.UNEXPLORED.value)

    fun guess(guess: Coord) {

        val hasGuessedAlready = guesses.map { it.toString() }.contains(guess.toString())
        when (hasGuessedAlready) {
            true -> guesses.removeAt(guesses.map { it.toString() }.indexOf(guess.toString()))
            false -> guesses.add(guess)
        }
    }

    fun explore(cell: Coord) {
        exploreAroundCell(cell, mutableListOf())
    }

    private fun exploreAroundCell(cell: Coord, alreadyExploredCells: MutableList<Coord>) {

        grid.markCellExplored(cell)
        val guessIndex = guesses.map { it.toString() }.indexOf(cell.toString())
        if(guessIndex != -1) guesses.removeAt(guessIndex)
        if(grid.getCellValue(cell) != Cell.EXPLORED) return

        val searchRows = 1.coerceAtLeast(cell.row - 1)..rows.coerceAtMost(cell.row + 1)
        val searchCols = 1.coerceAtLeast(cell.col - 1)..cols.coerceAtMost(cell.col + 1)

        val searchCells = mutableListOf<Coord>()

        searchRows.forEach { row ->
            searchCols.forEach { col ->
                val newCellToSearch = Coord(col, row)
                if (!alreadyExploredCells.map { it.toString() }.contains(newCellToSearch.toString())) {
                    searchCells.add(newCellToSearch)
                    alreadyExploredCells.add(newCellToSearch)
                }
            }
        }

        searchCells.forEach{ exploreAroundCell(it, alreadyExploredCells)}
    }
}