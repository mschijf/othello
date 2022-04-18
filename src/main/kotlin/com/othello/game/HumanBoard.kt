package com.othello.game

import java.util.*

private const val DELIMITER = ":"
class HumanBoard(boardString: String = ""): BitBoard(
    if (boardString.isEmpty()) null else boardString.split(DELIMITER)[0].toULong(),
    if (boardString.isEmpty()) null else boardString.split(DELIMITER)[1].toULong(),
    if (boardString.isEmpty()) null else boardString.split(DELIMITER)[2].toInt()) {

    fun toBoardString() = board[0].toString() + DELIMITER + board[1].toString() + DELIMITER + colorToMove.toString()

    //------------------------------------------------------------------------------------------------------------------

    private fun colRowToBit(col: Int, row: Int) = 1uL shl ((7 - row) * 8 + (7 - col))
    fun isBlackToMove() = colorToMove == BLACK
    fun isBlackDisc(col: Int, row: Int) = (colRowToBit(col, row) and board[BLACK]) != 0uL
    fun isWhiteDisc(col: Int, row: Int) = (colRowToBit(col, row) and board[WHITE]) != 0uL
    fun isEmpty(col: Int, row: Int) = ! (isWhiteDisc(col, row) || isBlackDisc(col,row))
    fun isPlayable(col: Int, row: Int) = (colRowToBit(col, row) and getAllCandidateMoves()) != 0uL

    //------------------------------------------------------------------------------------------------------------------

    private val moveStack = Stack<Move>()

    fun doMove(col: Int, row: Int) {
        val moves = generateMoves()
        val moveDone = moves.firstOrNull{colRowToBit(col, row) == it.discPlayed}
        if (moveDone != null) {
            doMove(moveDone)
            moveStack.push(moveDone)
        } else {
            throw Exception("Move from UI is not correct")
        }
    }

    fun takeBack() {
        val move = moveStack.pop()
        takeBack(move)
    }

    fun hasHistory() = moveStack.isNotEmpty()
}