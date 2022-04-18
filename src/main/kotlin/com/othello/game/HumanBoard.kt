package com.othello.game

import com.othello.bit.Bit64
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
    fun mustPass() = getAllCandidateMoves() == 0uL

    //------------------------------------------------------------------------------------------------------------------

    private val moveStack = Stack<Move>()

    private fun doMove(moveBit: Bit64) {
        val moves = generateMoves()
        val moveDone = moves.firstOrNull{moveBit == it.discPlayed}
        if (moveDone != null) {
            doMove(moveDone)
            moveStack.push(moveDone)
        } else {
            throw Exception("Move from UI is not correct")
        }
    }

    fun doMove(col: Int, row: Int) {
        doMove(colRowToBit(col, row))
    }

    fun doPassMove() {
        doMove(moveBit = 0uL)
    }

    fun takeBack() {
        takeBack(moveStack.pop())
    }

    fun hasHistory() = moveStack.isNotEmpty()
}