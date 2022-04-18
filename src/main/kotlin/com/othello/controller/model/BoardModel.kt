package com.othello.controller.model

import com.othello.game.*

const val MAX_ROW = 8
const val MAX_COL = 8

class BoardModel(board: HumanBoard)  {

    val fields = Array(MAX_ROW) { row -> Array(MAX_COL) { col -> getFieldModel(board, col, row) } }
    val colorToMove = if (board.isBlackToMove()) Color.BLACK else Color.WHITE

    private fun getFieldModel(board: HumanBoard, col: Int, row: Int): FieldModel {
        return FieldModel(
            col,
            row,
            if (board.isWhiteDisc(col, row)) Color.WHITE else  if (board.isBlackDisc(col, row)) Color.BLACK else Color.NONE,
            board.isPlayable(col,row))
    }
}

data class FieldModel(val col: Int, val row: Int, val color: Color, val playable: Boolean)
