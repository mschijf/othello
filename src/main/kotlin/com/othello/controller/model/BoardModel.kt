package com.othello.controller.model

import com.othello.game.*

const val MAX_ROW = 8
const val MAX_COL = 8

class BoardModel  {
    val numberOfRows = MAX_ROW
    val numberOfColumns = MAX_COL
    val fields = Array(MAX_ROW) { row -> Array(MAX_COL) { col -> FieldModel(col, row, Color.values()[col*row % 3], false) } }
}

data class FieldModel(val col: Int, val row: Int, val color: Color, val playable: Boolean)
