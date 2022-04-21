package com.othello.game

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BitBoardTest {

    @Test
    fun generateMovesTestOpeningPosition() {
        val board = BitBoard()
        val moves = board.generateMoves()
        //0x00_00_00_10_08_00_00_00uL, 0x00_00_00_08_10_00_00_00uL, 1) {
        assertEquals(4, moves.size)
        assertTrue(moves.any {it.discPlayed == 0x00_00_10_00_00_00_00_00uL})
        assertTrue(moves.any {it.discPlayed == 0x00_00_00_20_00_00_00_00uL})
        assertTrue(moves.any {it.discPlayed == 0x00_00_00_00_04_00_00_00uL})
        assertTrue(moves.any {it.discPlayed == 0x00_00_00_00_00_08_00_00uL})
    }

    @Test
    fun perftTest() {
        val board = BitBoard()
        assertEquals(4, board.perft(1))
        assertEquals(12, board.perft(2))
        assertEquals(56, board.perft(3))
        assertEquals(244, board.perft(4))
        assertEquals(1396, board.perft(5))
        assertEquals(8200, board.perft(6))
        assertEquals(55_092, board.perft(7))
        assertEquals(390_216, board.perft(8))
        assertEquals(3_005_288, board.perft(9))
        assertEquals(24_571_284, board.perft(10))
//        assertEquals(212_258_800, board.perft(11))
//        assertEquals(1_939_886_636, board.perft(12))

    //        val timer = Timer(true)
//        val board = BitBoard()
//        timer.startTimer("all")
//        var all = 0L
//        for (depth in 1..12) {
//            timer.startTimer("depth $depth")
//            val number = board.perft(depth)
//            timer.stopTimer("depth $depth", number)
//            all += number
//        }
//        timer.stopTimer("all", all)
    }

}