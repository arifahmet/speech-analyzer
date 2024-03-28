package org.arif.speechanalyzer.exception

abstract class SpeechAnalyzerException(val statusCode: Int, val errorMessage: String?) : RuntimeException()