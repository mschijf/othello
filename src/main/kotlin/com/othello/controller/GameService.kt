package com.othello.controller

import com.othello.controller.model.BoardModel
import com.othello.game.HumanBoard
import org.springframework.stereotype.Service

@Service
class GameService {
    var board = HumanBoard()

    fun getBoard(boardString: String): Pair<BoardModel, String> {
//        val board = HumanBoard(boardString)
        return BoardModel(board) to board.toBoardString()
    }

    fun getNewBoard(): Pair<BoardModel, String> {
        board = HumanBoard()
        return BoardModel(board) to board.toBoardString()
    }

    fun doMove(boardString: String, column: Int, row: Int): Pair<BoardModel, String> {
//        val board = HumanBoard(boardString)
        board.doMove(column, row)
        return BoardModel(board) to board.toBoardString()
    }

    fun takeBackLastMove(boardString: String): Pair<BoardModel, String> {
//        val board = HumanBoard(boardString)
        board.takeBack()
        return BoardModel(board) to board.toBoardString()
    }

//    fun computeAndExecuteNextMove(boardString: String): Pair<BoardModel, String> {
//        return BoardModel(BitBoard()) to ""
//    }
//
//    fun getComputeStatusInfo(boardString: String): ComputeStatusInfo {
//        return ComputeStatusInfo(genius.getInfo())
//    }
//
}