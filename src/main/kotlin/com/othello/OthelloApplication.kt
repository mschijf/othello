package com.othello

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OthelloApplication

fun main(args: Array<String>) {
    runApplication<OthelloApplication>(*args)
}

