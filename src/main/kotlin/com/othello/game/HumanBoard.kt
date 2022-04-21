package com.othello.game

import com.othello.bit.Bit64
import com.othello.bit.Bit64Math

//           BitBoard                               Human (0-based) Board
//
//  --- --- --- --- --- --- --- ---    RIJ --- --- --- --- --- --- --- ---
// |63 |62 |61 |60 |59 |58 |57 |56 |     0|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
// |55 |   |   |   |   |   |   |48 |     1|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
// |47 |   |   |   |   |   |   |40 |     2|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
// |39 |   |   |   |   |   |   |32 |     3|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
// |31 |   |   |   |   |   |   |24 |     4|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
// |23 |   |   |   |   |   |   |16 |     5|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
// |15 |   |   |   |   |   |   | 8 |     6|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
// | 7 | 6 | 5 | 4 | 3 | 2 | 1 | 0 |     7|   |   |   |   |   |   |   |   |
//  --- --- --- --- --- --- --- ---        --- --- --- --- --- --- --- ---
//                                          0   1   2   3   4   5   6   7   KOLOM


private const val DELIMITER = ":"

class HumanBoard(boardString: String = ""): BitBoard(
    if (boardString.isEmpty()) null else boardString.split(DELIMITER)[0].toULong(),
    if (boardString.isEmpty()) null else boardString.split(DELIMITER)[1].toULong(),
    if (boardString.isEmpty()) null else boardString.split(DELIMITER)[2].toInt()) {

    private val initialBoardString: String
    init {
        initialBoardString = toBoardString()
        val elements = boardString.split(DELIMITER)
        if (elements.size == 4) {
            val moveList = elements[3]
            for (i in 0 until moveList.length step 2) {
                val col = moveList[i].digitToInt()
                val row = moveList[i+1].digitToInt()
                if (col == 8 && row == 8) {
                    doPassMove()
                } else {
                    doMove(col, row)
                }
            }
        }
    }

    fun toBoardString() = board[0].toString() + DELIMITER + board[1].toString() + DELIMITER + colorToMove.toString()
    fun toBoardStatusString() =
        initialBoardString +
                DELIMITER +
                moveStack
                    .map { if (it.isPass()) "88" else "%d%d".format(bitToCol(it.discPlayed), bitToRow(it.discPlayed)) }
                    .joinToString("")

    //------------------------------------------------------------------------------------------------------------------

    private fun colRowToBit(col: Int, row: Int) = 1uL shl ((7 - row) * 8 + (7 - col))
    private fun bitToCol(bit: Bit64) = 7 - (Bit64Math.mostRightBitIndex(bit) % 8)
    private fun bitToRow(bit: Bit64) = 7 - (Bit64Math.mostRightBitIndex(bit) / 8)

    fun isBlackToMove() = colorToMove == BLACK
    fun isBlackDisc(col: Int, row: Int) = (colRowToBit(col, row) and board[BLACK]) != 0uL
    fun isWhiteDisc(col: Int, row: Int) = (colRowToBit(col, row) and board[WHITE]) != 0uL

    fun isPlayable(col: Int, row: Int) = (colRowToBit(col, row) and getAllCandidateMoves()) != 0uL
    fun mustPass() = getAllCandidateMoves() == 0uL

    //------------------------------------------------------------------------------------------------------------------

    private fun doMove(moveBit: Bit64) {
        val moves = generateMoves()
        val moveDone = moves.firstOrNull{moveBit == it.discPlayed}
        if (moveDone != null) {
            doMove(moveDone)
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

    fun hasHistory() = moveStack.isNotEmpty()
}