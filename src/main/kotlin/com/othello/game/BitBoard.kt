package com.othello.game

import com.othello.bit.Bit64
import com.othello.bit.Bit64Math
import java.util.*

private const val RIGHT_BORDER = 0x01_01_01_01_01_01_01_01uL
private const val LEFT_BORDER  = 0x80_80_80_80_80_80_80_80uL
private const val TOP_BORDER = 0x00_00_00_00_00_00_00_FFuL
private const val BOTTOM_BORDER = 0xFF_00_00_00_00_00_00_00uL

private val VERTICAL_MIDDLE = (LEFT_BORDER or RIGHT_BORDER).inv()

private const val WEST      = 1  //1 shift to left
private const val NORTHEAST = 7  //7 shift to left
private const val NORTH     = 8  //8 shift to left
private const val NORTHWEST = 9  //9 shift to left
private const val EAST      = 1  //1 shift to right
private const val SOUTHWEST = 7  //7 shift to right
private const val SOUTH     = 8  //8 shift to right
private const val SOUTHEAST = 9  //9 shift to right

internal const val WHITE = 0
internal const val BLACK = 1

open class BitBoard (initBoardWhite: Bit64?=null, initBoardBlack: Bit64?=null, initColorToMove: Int?=null) {

    protected val board: Array<Bit64>
    protected var colorToMove: Int
    protected val moveStack = Stack<Move>()

    init {
        if ((initBoardBlack != null) && initBoardWhite != null && initColorToMove != null) {
            board = arrayOf(initBoardWhite, initBoardBlack)
            colorToMove = initColorToMove
        } else {
            board = arrayOf(0x00_00_00_10_08_00_00_00uL, 0x00_00_00_08_10_00_00_00uL)
            colorToMove = BLACK
        }
    }

    private fun getLeftHittingCandidate(direction: Int, bbToMove: Bit64, bbCapturable: Bit64, bbEmpty: Bit64): Bit64 {
        var candidate = 0uL
        var loop = (bbToMove shr direction) and bbCapturable
        while (loop != 0uL) {
            loop = loop shr direction
            candidate = candidate or (loop and bbEmpty)
            loop = loop and bbCapturable
        }
        return candidate
    }

    private fun getLeftCapture(direction: Int, bbOpponent: Bit64, bbMove: Bit64): Bit64 {
        var allCaptures = 0uL
        var capture = bbMove shl direction
        do {
            allCaptures = allCaptures or capture
            capture = capture shl direction
        } while ((capture and bbOpponent) != 0uL)
        return allCaptures
    }

    private fun getRightHittingCandidate(direction: Int, bbToMove: Bit64, bbCapturable: Bit64, bbEmpty: Bit64): Bit64 {
        var candidate = 0uL
        var loop = (bbToMove shl direction) and bbCapturable
        while (loop != 0uL) {
            loop = loop shl direction
            candidate = candidate or (loop and bbEmpty)
            loop = loop and bbCapturable
        }
        return candidate
    }

    private fun getRightCapture(direction: Int, bbOpponent: Bit64, bbMove: Bit64): Bit64 {
        var allCaptures = 0uL
        var capture = bbMove shr direction
        do {
            allCaptures = allCaptures or capture
            capture = capture shr direction
        } while ((capture and bbOpponent) != 0uL)
        return allCaptures
    }

    fun generateMoves(): List<Move> {
        val moveList = mutableListOf<Move>()
        val bbToMove = board[colorToMove]
        val bbOpponent = board[1-colorToMove]
        val bbEmpty = (bbToMove or bbOpponent).inv()
        val bbWithoutLeftRightBorder = bbOpponent and VERTICAL_MIDDLE

        val candWest = getLeftHittingCandidate(WEST, bbToMove, bbWithoutLeftRightBorder, bbEmpty)
        val candNorthEast = getLeftHittingCandidate(NORTHEAST, bbToMove, bbWithoutLeftRightBorder, bbEmpty)
        val candNorth = getLeftHittingCandidate(NORTH, bbToMove, bbOpponent, bbEmpty)
        val candNorthWest = getLeftHittingCandidate(NORTHWEST, bbToMove, bbWithoutLeftRightBorder, bbEmpty)

        val candEast = getRightHittingCandidate(EAST, bbToMove, bbWithoutLeftRightBorder, bbEmpty)
        val candSouthWest = getRightHittingCandidate(SOUTHWEST, bbToMove, bbWithoutLeftRightBorder, bbEmpty)
        val candSouth = getRightHittingCandidate(SOUTH, bbToMove, bbOpponent, bbEmpty)
        val candSouthEast = getRightHittingCandidate(SOUTHEAST, bbToMove, bbWithoutLeftRightBorder, bbEmpty)
        var candAll = candWest or candNorthEast or candNorth or candNorthWest or candEast or candSouthWest or candSouth or candSouthEast

        while (candAll != 0uL) {
            var allCaptures = 0uL
            val bbMove = Bit64Math.smallestBit(candAll)
            if ((bbMove and candWest) != 0uL)
                allCaptures = allCaptures or getLeftCapture(WEST, bbOpponent, bbMove)
            if ((bbMove and candNorthEast) != 0uL)
                allCaptures = allCaptures or getLeftCapture(NORTHEAST, bbOpponent, bbMove)
            if ((bbMove and candNorth) != 0uL)
                allCaptures = allCaptures or getLeftCapture(NORTH, bbOpponent, bbMove)
            if ((bbMove and candNorthWest) != 0uL)
                allCaptures = allCaptures or getLeftCapture(NORTHWEST, bbOpponent, bbMove)

            if ((bbMove and candEast) != 0uL)
                allCaptures = allCaptures or getRightCapture(EAST, bbOpponent, bbMove)
            if ((bbMove and candSouthWest) != 0uL)
                allCaptures = allCaptures or getRightCapture(SOUTHWEST, bbOpponent, bbMove)
            if ((bbMove and candSouth) != 0uL)
                allCaptures = allCaptures or getRightCapture(SOUTH, bbOpponent, bbMove)
            if ((bbMove and candSouthEast) != 0uL)
                allCaptures = allCaptures or getRightCapture(SOUTHEAST, bbOpponent, bbMove)

            moveList.add(Move(allCaptures, bbMove))
            candAll = candAll xor bbMove
        }
        if (moveList.isEmpty())
            return listOf(Move(discsFlipped = 0uL, discPlayed = 0uL))
        return moveList
    }

    //todo: this one is called multiple (64) times when UI asks for it.
    protected fun getAllCandidateMoves(): Bit64 {
        val bbToMove = board[colorToMove]
        val bbOpponent = board[1 - colorToMove]
        val bbEmpty = (bbToMove or bbOpponent).inv()
        val bbWithoutSideBorder = bbOpponent and VERTICAL_MIDDLE

        val candWest = getLeftHittingCandidate(WEST, bbToMove, bbWithoutSideBorder, bbEmpty)
        val candNorthEast = getLeftHittingCandidate(NORTHEAST, bbToMove, bbWithoutSideBorder, bbEmpty)
        val candNorth = getLeftHittingCandidate(NORTH, bbToMove, bbOpponent, bbEmpty)
        val candNorthWest = getLeftHittingCandidate(NORTHWEST, bbToMove, bbWithoutSideBorder, bbEmpty)
        val candEast = getRightHittingCandidate(EAST, bbToMove, bbWithoutSideBorder, bbEmpty)
        val candSouthWest = getRightHittingCandidate(SOUTHWEST, bbToMove, bbWithoutSideBorder, bbEmpty)
        val candSouth = getRightHittingCandidate(SOUTH, bbToMove, bbOpponent, bbEmpty)
        val candSouthEast = getRightHittingCandidate(SOUTHEAST, bbToMove, bbWithoutSideBorder, bbEmpty)
        return candWest or candNorthEast or candNorth or candNorthWest or candEast or candSouthWest or candSouth or candSouthEast
    }

    fun doMove(move: Move) {
        board[colorToMove] = board[colorToMove] xor (move.discsFlipped or move.discPlayed)
        colorToMove = 1 - colorToMove
        board[colorToMove] = board[colorToMove] xor move.discsFlipped
        moveStack.push(move)
    }

    fun takeBack() {
        val move = moveStack.pop()
        board[colorToMove] = board[colorToMove] xor move.discsFlipped
        colorToMove = 1 - colorToMove
        board[colorToMove] = board[colorToMove] xor (move.discsFlipped or move.discPlayed)
    }

    fun isEndOfGame(): Boolean {
        if ((board[WHITE] or board[BLACK]).inv() == 0uL)
            return true
        val top = moveStack.size
        return moveStack.size > 2 && moveStack[top-1].isPass() && moveStack[top-2].isPass()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //    depth 1                               :    20 ms -->               4
    //    depth 2                               :     0 ms -->              12
    //    depth 3                               :     0 ms -->              56
    //    depth 4                               :     1 ms -->             244
    //    depth 5                               :     8 ms -->           1.396
    //    depth 6                               :    10 ms -->           8.200
    //    depth 7                               :    33 ms -->          55.092
    //    depth 8                               :    67 ms -->         390.216
    //    depth 9                               :   429 ms -->       3.005.288
    //    depth 10                              : 2.373 ms -->      24.571.284
    //    depth 11                             : 16.196 ms -->     212.258.800
    //    depth 12                            : 149.402 ms -->   1.939.886.636
    //    depth 13                          : 1.406.857 ms -->  18.429.641.748
    //    all                               : 1.575.451 ms -->  20.609.818.976
    //
    //    speed: 13.081.853 per second
    //
    //    see also http://www.aartbik.com/strategy.php
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun perft(depth: Int): Long {
        if (depth == 0) {
            return 1
        }
        if (isEndOfGame()) {
            return 1
        }
        var nodeCount = 0L
        val moves = generateMoves()
        for (move in moves) {
            doMove(move)
            nodeCount += perft(depth-1)
            takeBack()
        }
        return nodeCount
    }
}