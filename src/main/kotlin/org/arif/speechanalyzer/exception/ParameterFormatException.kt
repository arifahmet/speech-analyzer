package org.arif.speechanalyzer.exception

import org.springframework.http.HttpStatus

class ParameterFormatException :
    SpeechAnalyzerException(HttpStatus.BAD_REQUEST.value(), ErrorMessageEnum.PARAMETER_FORMAT_NOT_VALID.message) {
}