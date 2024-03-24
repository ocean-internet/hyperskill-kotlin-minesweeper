package minesweeper

const val INVALID_COORDINATE = "Invalid coordinate"

class Coord {

    val col: Int
    val row: Int

    constructor(col: Int, row: Int) {
        this.col = col
        this.row = row
    }

    constructor(coord: String) {
        val parts = coord.trim().split(" ")
            .map { it.trim() }
            .map { it.ifBlank { throw RuntimeException(INVALID_COORDINATE) } }
            .map { it.toInt() }

        if (parts.size != 2) throw RuntimeException(INVALID_COORDINATE)

        this.col = parts[0]
        this.row = parts[1]
    }

    override fun toString(): String {
        return "x:$col, y:$row"
    }
}