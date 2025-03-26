package com.example.banktraining.controllers

import com.example.banktraining.entities.Message
import com.example.banktraining.services.MessageService
import com.example.banktraining.utils.toAsciiTable
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI


@RestController
@RequestMapping("/")
class MessageController(private val service: MessageService) {
    @GetMapping
    fun listMessages() = ResponseEntity.ok(service.findMessages())

    @PostMapping
    fun post(@RequestBody message: Message): ResponseEntity<Message> {
        val savedMessage = service.save(message)
        return ResponseEntity.created(URI("/${savedMessage.id}")).body(savedMessage)
    }

    @GetMapping("/{id}")
    fun getMessage(@PathVariable id: String): ResponseEntity<Message> =
        service.findMessageById(id).toResponseEntity()

    private fun Message?.toResponseEntity(): ResponseEntity<Message> =
        this?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping("/data")
    fun displayData(request: HttpServletRequest): Any {
        val messages = service.getAllRawMessages()
        val table = messages.toAsciiTable()

        // Since HTML collapses whitespace by default, we need to wrap the table in <pre> tags
        return if (request.getHeader("Accept")?.contains("text/html") == true) {
            """
        <!DOCTYPE html>
        <html>
        <head><title>Database Contents</title></head>
        <body>
            <pre>$table</pre>
        </body>
        </html>
        """.trimIndent()
        } else {
            table
        }
    }
}