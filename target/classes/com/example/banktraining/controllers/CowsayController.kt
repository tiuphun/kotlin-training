package com.example.banktraining.controllers

import com.example.banktraining.entities.Message
import org.springframework.web.bind.annotation.*
import com.github.ricksbrown.cowsay.Cowsay
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@RestController
@RequestMapping("/cowsay")
class CowsayController {

    // List of known cowfiles from the library
    private val availableCowfiles = listOf(
        "default", "dragon", "elephant", "ghostbusters",
        "kitty", "stegosaurus", "turkey", "tux", "vader",
        "moose", "koala", "sheep", "flaming-sheep", "cheese"
    )

    @PostMapping
    fun cowSay(
        @RequestBody message: Message,
        @RequestParam(required = false) cowfile: String?,
        @RequestParam(required = false) eyes: String?,
        @RequestParam(required = false) tongue: String?
    ): String {
        // Create arguments for cowsay
        val args = mutableListOf<String>().apply {
            add("-f")
            add(cowfile ?: availableCowfiles.random())
            eyes?.let { add("-e"); add(it) }
            tongue?.let { add("-T"); add(it) }
            add(message.text)
        }

        // Redirect System.out to capture the output
        val originalOut = System.out
        return try {
            val baos = ByteArrayOutputStream()
            System.setOut(PrintStream(baos))
            Cowsay.main(args.toTypedArray())
            baos.toString().trim()
        } finally {
            System.setOut(originalOut) // Restore original System.out
        }
    }
}