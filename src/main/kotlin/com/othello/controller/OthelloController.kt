package com.othello.controller

import com.othello.controller.model.BoardModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse as HttpServletResponse1

const val BOARD_COOKIE = "BOARDSTATUS"
const val REQUESTPATH_BASE = "api/v1"
const val DEFAULT_BOARD = ""

@RestController
@RequestMapping(REQUESTPATH_BASE)
class OthelloController @Autowired constructor(private val gameService: GameService) {

    @GetMapping("/board/")
    fun getBoard(@CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD) boardStatusString: String): BoardModel {
        val (model, _) = gameService.getBoard(boardStatusString)
        return model
    }

    @PostMapping("/board/")
    fun newBoard(httpServletResponse: HttpServletResponse1): BoardModel {
        val (model, persistanceString) =  gameService.getNewBoard()
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

    @PostMapping("/move/{column}")
    fun doMove(httpServletResponse: HttpServletResponse1,
               @CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD) boardStatusString: String,
               @PathVariable(name = "column") column: Int): BoardModel {
        val (model, persistanceString) =  gameService.doMove(boardStatusString, column)
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

    @PostMapping("/move/takeback/")
    fun takeBackLastMove(httpServletResponse: HttpServletResponse1,
                         @CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD) boardStatusString: String): BoardModel {
        val (model, persistanceString) =  gameService.takeBackLastMove(boardStatusString)
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

    @PostMapping("/move/compute/{level}")
    fun computeAndExecuteNextMove(httpServletResponse: HttpServletResponse1,
                                  @CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD) boardStatusString: String,
                                  @PathVariable(name = "level") level: Int): BoardModel {
        val (model, persistanceString) =  gameService.computeAndExecuteNextMove(boardStatusString)
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

//    @GetMapping("/compute/info/")
//    fun getComputeStatusInfo(@CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD) boardStatusString: String): ComputeStatusInfo {
//        return gameService.getComputeStatusInfo(boardStatusString)
//    }
//

    private fun getNewCookie(persistanceString: String): Cookie {
        val cookie = Cookie(BOARD_COOKIE, persistanceString)
        cookie.maxAge = 3600*24*365
        cookie.path = "/"
        return cookie
    }
}

