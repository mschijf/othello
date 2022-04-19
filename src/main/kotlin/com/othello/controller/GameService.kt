package com.othello.controller

import com.othello.controller.model.BoardModel
import com.othello.game.HumanBoard
import org.springframework.stereotype.Service

@Service
class GameService {
    fun getBoard(boardStatusString: String): Pair<BoardModel, String> {
        val board = HumanBoard(boardStatusString)
        return BoardModel(board) to board.toBoardStatusString()
    }

    fun getNewBoard(): Pair<BoardModel, String> {
        val board = HumanBoard()
        return BoardModel(board) to board.toBoardStatusString()
    }

    fun doMove(boardStatusString: String, column: Int, row: Int): Pair<BoardModel, String> {
        val board = HumanBoard(boardStatusString)
        board.doMove(column, row)
        return BoardModel(board) to board.toBoardStatusString()
    }

    fun doPassMove(boardStatusString: String): Pair<BoardModel, String> {
        val board = HumanBoard(boardStatusString)
        board.doPassMove()
        return BoardModel(board) to board.toBoardStatusString()
    }


    fun takeBackLastMove(boardStatusString: String): Pair<BoardModel, String> {
        val board = HumanBoard(boardStatusString)
        board.takeBack()
        return BoardModel(board) to board.toBoardStatusString()
    }

//    fun computeAndExecuteNextMove(boardStatusString : String): Pair<BoardModel, String> {
//        return BoardModel(BitBoard()) to ""
//    }
//
//    fun getComputeStatusInfo(boardStatusString : String): ComputeStatusInfo {
//        return ComputeStatusInfo(genius.getInfo())
//    }
//
}