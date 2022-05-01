package com.othello.controller

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Template
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import com.github.jknack.handlebars.io.TemplateLoader
import com.othello.controller.model.BoardModel
import com.othello.game.HumanBoard
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OthelloHTMLController() {
    @GetMapping("/")
    fun index(): String {
        val loader: TemplateLoader = ClassPathTemplateLoader("/handlebars", ".hbs")
        val handlebars = Handlebars(loader)
        val template: Template = handlebars.compile("othello")
        return template.apply(null)
    }
}


