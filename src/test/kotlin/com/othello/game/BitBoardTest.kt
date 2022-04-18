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
}