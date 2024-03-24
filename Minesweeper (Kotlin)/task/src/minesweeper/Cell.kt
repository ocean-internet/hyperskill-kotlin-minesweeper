package minesweeper

enum class Cell(val value: Char) {

    MINE('X'),
    GUESS('*'),

    UNEXPLORED('.'),
    EXPLORED('/'),

    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    ;
}