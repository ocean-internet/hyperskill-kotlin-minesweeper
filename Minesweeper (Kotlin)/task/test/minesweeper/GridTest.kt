package minesweeper

import minesweeper.Cell.*
import org.junit.Assert.*
import org.junit.Test

class GridTest {

    @Test
    fun `it should make grid`() {
        val grid = Grid(3, 3)

        assertEquals(
            """
            ...
            ...
            ...
            """.trimIndent(),
            grid.toString()
        )
    }

    @Test
    fun `it should set cell value`() {
        val grid = Grid(3, 3)

        grid.setCellValue(Coord(2, 2), MINE)

        assertEquals(
            """
            ...
            .${MINE.value}.
            ...
            """.trimIndent(),
            grid.toString()
        )
    }

    @Test
    fun `it should get cell value`() {
        val grid = Grid(3, 3)
        val cell = Coord(2, 2)

        assertEquals(UNEXPLORED, grid.getCellValue(cell))

        val newValue = MINE
        grid.setCellValue(cell, newValue)

        assertEquals(newValue, grid.getCellValue(cell))
    }

    @Test
    fun `it should find mines - 1`() {
        val grid = Grid(3, 3)

        grid.setCellValue(Coord(1, 1), MINE)

        assertEquals(1, grid.countMinesAroundCell(Coord(2, 2)))
    }

    @Test
    fun `it should find mines - 2`() {
        val grid = Grid(3, 3)

        grid.setCellValue(Coord(1, 1), MINE)
        grid.setCellValue(Coord(3, 3), MINE)

        assertEquals(2, grid.countMinesAroundCell(Coord(2, 2)))
    }

    @Test
    fun `it should find mines - 3`() {
        val grid = Grid(3, 3)

        grid.setCellValue(Coord(1, 1), MINE)
        grid.setCellValue(Coord(3, 3), MINE)
        grid.setCellValue(Coord(1, 3), MINE)

        assertEquals(3, grid.countMinesAroundCell(Coord(2, 2)))
    }

    @Test
    fun `it should find mines - 4`() {
        val grid = Grid(3, 3)

        grid.setCellValue(Coord(1, 1), MINE)
        grid.setCellValue(Coord(3, 3), MINE)
        grid.setCellValue(Coord(1, 3), MINE)
        grid.setCellValue(Coord(3, 1), MINE)

        assertEquals(4, grid.countMinesAroundCell(Coord(2, 2)))
    }

    @Test
    fun `it should find mines - 8`() {
        val grid = Grid(3, 3)

        grid.setCellValue(Coord(1, 1), MINE)
        grid.setCellValue(Coord(1, 2), MINE)
        grid.setCellValue(Coord(1, 3), MINE)
        grid.setCellValue(Coord(2, 1), MINE)
        grid.setCellValue(Coord(2, 3), MINE)
        grid.setCellValue(Coord(3, 1), MINE)
        grid.setCellValue(Coord(3, 2), MINE)
        grid.setCellValue(Coord(3, 3), MINE)

        assertEquals(8, grid.countMinesAroundCell(Coord(2, 2)))
    }

    @Test
    fun `it should mark cell explored`() {
        val grid = Grid(3, 3)
        val cell = Coord(2, 2)

        grid.markCellExplored(cell)

        assertEquals(EXPLORED, grid.getCellValue(cell))
    }

    @Test
    fun `it should mark cell explored with mine count - 1`() {
        val grid = Grid(3, 3)
        val cell = Coord(2, 2)

        grid.setCellValue(Coord(1, 1), MINE)

        grid.markCellExplored(cell)

        assertEquals(ONE, grid.getCellValue(cell))
    }

    @Test
    fun `it should mark cell explored with mine count - 8`() {
        val grid = Grid(3, 3)
        val cell = Coord(2, 2)

        grid.setCellValue(Coord(1, 1), MINE)
        grid.setCellValue(Coord(1, 2), MINE)
        grid.setCellValue(Coord(1, 3), MINE)
        grid.setCellValue(Coord(2, 1), MINE)
        grid.setCellValue(Coord(2, 3), MINE)
        grid.setCellValue(Coord(3, 1), MINE)
        grid.setCellValue(Coord(3, 2), MINE)
        grid.setCellValue(Coord(3, 3), MINE)

        grid.markCellExplored(cell)

        assertEquals(EIGHT, grid.getCellValue(cell))
    }

    @Test
    fun `it should throw GameEnded error`() {
        val error = assertThrows(GameEndedException::class.java) {
            val grid = Grid(3, 3)
            val cell = Coord(2, 2)

            grid.setCellValue(cell, MINE)

            grid.markCellExplored(cell)
        }

        assertEquals(ERROR_EXPLORED_MINE, error.message)
    }
}