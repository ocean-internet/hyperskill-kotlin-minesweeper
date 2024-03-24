package minesweeper

const val ERROR_INVALID_GRID = "Invalid grid"
const val ERROR_EXPLORED_MINE = "You stepped on a mine and failed!"

private const val MAX_COLS_ROWS = 99

class Grid(val cols: Int, private val rows: Int) {

    init {
        fun isValid(value: Int) = value in 2..MAX_COLS_ROWS
        if (!(isValid(cols) && isValid(rows))) throw RuntimeException(ERROR_INVALID_GRID)
    }

    val size: Int get() = cols * rows

    private val _grid = (1..rows)
        .map {
            (1..cols)
                .map { Cell.UNEXPLORED }
                .toMutableList()
        }
        .toMutableList()

    val grid: MutableList<MutableList<Cell>>
        get() = _grid.map { it.toMutableList() }.toMutableList()

    fun clear() {
        (1..rows).forEach { row ->
            (1..cols).forEach { col ->
                setCellValue(Coord(col, row), Cell.UNEXPLORED)
            }
        }
    }

    fun getCellValue(coord: Coord): Cell {
        return _grid[coord.row - 1][coord.col - 1]
    }

    fun setCellValue(cell: Coord, value: Cell) {
        _grid[cell.row - 1][cell.col - 1] = value
    }

    fun countMinesAroundCell(cell: Coord): Int {
        val searchRows = 1.coerceAtLeast(cell.row - 1)..rows.coerceAtMost(cell.row + 1)
        val searchCols = 1.coerceAtLeast(cell.col - 1)..cols.coerceAtMost(cell.col + 1)
        var mineCount = 0;

        searchRows.forEach { row ->
            searchCols.forEach { col ->
                if (getCellValue(Coord(col, row)) == Cell.MINE) mineCount++
            }
        }
        return mineCount
    }

    fun markCellExplored(cell: Coord) {
        if (getCellValue(cell) == Cell.MINE) throw GameEndedException(ERROR_EXPLORED_MINE)

        val mineCount = countMinesAroundCell(cell)
        val newCellValue = if (mineCount == 0) Cell.EXPLORED else cellValueFromMineCount(mineCount)

        setCellValue(cell, newCellValue)
    }

    private fun cellValueFromMineCount(mineCount: Int) =
        Cell.values().find { it.value.toString() == mineCount.toString() } ?: throw RuntimeException()

    override fun toString(): String = grid.joinToString("\n") { row ->
        row.joinToString("") { cell ->
            cell.value.toString()
        }
    }
}