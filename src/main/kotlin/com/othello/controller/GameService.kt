package com.othello.controller

import com.othello.controller.model.BoardModel
import org.springframework.stereotype.Service

@Service
class GameService {
    fun getBoard(boardStatusString: String): Pair<BoardModel, String> {
        return BoardModel() to ""
    }

    fun getNewBoard(): Pair<BoardModel, String> {
        return BoardModel() to ""
    }

    fun doMove(boardStatusString: String, column: Int): Pair<BoardModel, String> {
        return BoardModel() to ""
    }

    fun takeBackLastMove(boardStatusString: String): Pair<BoardModel, String> {
        return BoardModel() to ""
    }

    fun computeAndExecuteNextMove(boardStatusString: String): Pair<BoardModel, String> {
        return BoardModel() to ""
    }
//
//    fun getComputeStatusInfo(boardStatusString: String): ComputeStatusInfo {
//        return ComputeStatusInfo(genius.getInfo())
//    }
//
}