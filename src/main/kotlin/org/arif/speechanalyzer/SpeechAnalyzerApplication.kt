package org.arif.speechanalyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpeechAnalyzerApplication

fun main(args: Array<String>) {
    runApplication<SpeechAnalyzerApplication>(*args)
}
