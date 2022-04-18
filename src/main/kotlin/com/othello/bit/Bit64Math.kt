package com.othello.bit

typealias Bit64 = ULong

object Bit64Math {
    private const val LEASTSIGNIFICANT16 = 0x00_00_00_00_00_00_FF_FFuL //the 16 right-most bits are on
    private const val LEASTSIGNIFICANT32 = 0x00_00_00_00_FF_FF_FF_FFuL //the 32 right-most bits are on
    private const val LEASTSIGNIFICANT48 = 0x00_00_FF_FF_FF_FF_FF_FFuL //the 48 right-most bits are on

    private val mostRightBitIndex = Array(65536) {bv -> mostRightBitIndexInFirst16Bits(bv)}
    private val bitCount = Array(65536) {bv -> bitCountInFirst16Bits(bv)}
    private fun bitCountInFirst16Bits(bitValue: Int): Int {
        var count = 0
        for (bitIndex in 0..15)
            if ((1 shl bitIndex) and bitValue != 0)
                count++
        return count
    }

    fun smallestBit(bitValue: Bit64) = (bitValue - 1uL).inv() and bitValue

    private fun mostRightBitIndexInFirst16Bits(bitValue: Int): Int {
        for (bitIndex in 0..15)
            if ((1 shl bitIndex) and bitValue != 0)
                return bitIndex
        return -49
    }

    fun numberOfBits(bitValue: Bit64) =
            bitCount[(bitValue and LEASTSIGNIFICANT16).toInt()] +
            bitCount[((bitValue shr 16) and LEASTSIGNIFICANT16).toInt()] +
            bitCount[((bitValue shr 32) and LEASTSIGNIFICANT16).toInt()] +
            bitCount[((bitValue shr 48) and LEASTSIGNIFICANT16).toInt()]

    fun mostRightBitIndex(bitValue: Bit64): Int {
        if (bitValue and LEASTSIGNIFICANT32 != 0uL) {
            if (bitValue and LEASTSIGNIFICANT16 != 0uL)
                return mostRightBitIndex[(bitValue and LEASTSIGNIFICANT16).toInt()]
            return 16 + mostRightBitIndex[((bitValue shr 16) and LEASTSIGNIFICANT16).toInt()]
        } else {
            if (bitValue and LEASTSIGNIFICANT48 != 0uL)
                return 32 + mostRightBitIndex[((bitValue shr 32) and LEASTSIGNIFICANT16).toInt()]
            return 48 + mostRightBitIndex[((bitValue shr 48) and LEASTSIGNIFICANT16).toInt()]
        }
    }
}

