package com.othello.game

import com.othello.bit.Bit64

class Move(
    val discsFlipped: Bit64,
    val discPlayed: Bit64) {

    fun isPass() = discPlayed == 0uL
}
