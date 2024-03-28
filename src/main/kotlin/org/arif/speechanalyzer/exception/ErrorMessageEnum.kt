package org.arif.speechanalyzer.exception

enum class ErrorMessageEnum(var message: String) {
    PARAMETER_SIZE_MISMATCH("CSV file parameter size must be 4."),
    PARAMETER_FORMAT_NOT_VALID("Parameter format is invalid. Check CSV file.");
}