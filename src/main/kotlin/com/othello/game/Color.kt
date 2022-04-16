package com.othello.game

enum class Color {
    WHITE {
        override fun opponent() = BLACK
    },
    BLACK {
        override fun opponent() = WHITE
    },
    NONE {
        override fun opponent() = NONE
    };

    abstract fun opponent(): Color
}
