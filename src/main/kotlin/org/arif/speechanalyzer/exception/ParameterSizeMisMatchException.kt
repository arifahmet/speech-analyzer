package org.arif.speechanalyzer.exception

import org.springframework.http.HttpStatus

class ParameterSizeMisMatchException :
     SpeechAnalyzerException(HttpStatus.BAD_REQUEST.value(), ErrorMessageEnum.PARAMETER_SIZE_MISMATCH.message)