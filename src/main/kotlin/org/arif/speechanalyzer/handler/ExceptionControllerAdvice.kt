package org.arif.speechanalyzer.handler

import org.arif.speechanalyzer.exception.SpeechAnalyzerException
import org.arif.speechanalyzer.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler
    fun handleIllegalStateException(e: SpeechAnalyzerException): ResponseEntity<ErrorResponse> {

        val errorMessage = ErrorResponse(
            e.errorMessage
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}