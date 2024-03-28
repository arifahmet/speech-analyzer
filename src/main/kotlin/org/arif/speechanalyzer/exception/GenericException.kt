package org.arif.speechanalyzer.exception

class GenericException(statusCode: Int, errorMessage: String?) : SpeechAnalyzerException(statusCode, errorMessage)