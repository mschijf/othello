package com.othello.bit

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Bit64MathTest {

    @Test
    fun tryULongBitWise() {
        assertEquals(0x00_00_00_00_00_00_80_00uL, 1uL shl 15)
        assertEquals(0x00_00_00_00_80_00_00_00uL, 1uL shl 31)
        assertEquals(0x00_00_80_00_00_00_00_00uL, 1uL shl 47)
        assertEquals(0x00_00_00_00_00_00_FF_FFuL,  (1uL shl 16) - 1uL )
    }

    @Test
    fun mostRightBitIndexTest() {
        assertEquals(15, Bit64Math.mostRightBitIndex((1uL shl 15) or (1uL shl 56)))
        assertEquals(15, Bit64Math.mostRightBitIndex(0x01_00_00_00_00_00_80_00uL))
        assertEquals(16, Bit64Math.mostRightBitIndex(0x01_00_00_00_00_01_00_00uL))
        assertEquals(56, Bit64Math.mostRightBitIndex(0x01_00_00_00_00_00_00_00uL))
        assertEquals(-1, Bit64Math.mostRightBitIndex(0x00_00_00_00_00_00_00_00uL))
    }

    @Test
    fun countBitTest() {
        assertEquals(8, Bit64Math.numberOfBits(0x01_01_01_01_01_01_01_01uL))
        assertEquals(64, Bit64Math.numberOfBits(0xFF_FF_FF_FF_FF_FF_FF_FFuL))
        assertEquals(0, Bit64Math.numberOfBits(0x00_00_00_00_00_00_00_00uL))
    }

    @Test
    fun smallesBitTest() {
        assertEquals(0x00_00_00_00_00_00_00_01uL, Bit64Math.smallestBit(0x01_01_01_01_01_01_01_01uL))
        assertEquals(0x00_00_00_00_00_00_00_01uL, Bit64Math.smallestBit(0xFF_FF_FF_FF_FF_FF_FF_FFuL))
        assertEquals(0x00_00_00_00_00_00_00_10uL, Bit64Math.smallestBit(0x01_01_01_01_01_01_01_10uL))
        assertEquals(0uL, Bit64Math.smallestBit(0x00_00_00_00_00_00_00_00uL))
    }
}