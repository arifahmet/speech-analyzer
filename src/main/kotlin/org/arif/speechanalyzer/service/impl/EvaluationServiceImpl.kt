package org.arif.speechanalyzer.service.impl

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.arif.speechanalyzer.exception.GenericException
import org.arif.speechanalyzer.exception.ParameterFormatException
import org.arif.speechanalyzer.exception.ParameterSizeMisMatchException
import org.arif.speechanalyzer.exception.SpeechAnalyzerException
import org.arif.speechanalyzer.model.ProcessesCsvFilesResponse
import org.arif.speechanalyzer.model.SpeechModel
import org.arif.speechanalyzer.service.EvaluationService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class EvaluationServiceImpl : EvaluationService {
    companion object {
        const val CSV_DELIMITER = ';'
        const val MOST_SPEECH_YEAR = 2013
        const val MOST_SECURITY_KEYWORD = "homeland security"
        const val CUSTOM_DATE_FORMAT = "yyyy-MM-dd"
    }

    override fun processesCsvFiles(urlList: List<String>): ProcessesCsvFilesResponse {
        val speeches = ArrayList<SpeechModel>()
        readAllCsvFiles(urlList, speeches)
        return ProcessesCsvFilesResponse(
            calculateMostSpeeches(speeches),
            calculateMostSecurity(speeches),
            calculateMostLeastWordy(speeches)
        )
    }

    private fun readAllCsvFiles(
        urlList: List<String>,
        speeches: ArrayList<SpeechModel>
    ) {
        urlList.forEach {
            try {
                speeches.addAll(readCsv(File(it).inputStream()))
            } catch (e: SpeechAnalyzerException) {
                throw e
            } catch (e: IOException) {
                throw GenericException(HttpStatus.BAD_REQUEST.value(), e.message)
            } catch (e: Exception) {
                throw GenericException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
            }
        }
    }

    private fun readCsv(inputStream: InputStream): List<SpeechModel> =
        CSVFormat.Builder.create(CSVFormat.newFormat(CSV_DELIMITER)).apply {
            setIgnoreSurroundingSpaces(true)
        }.build().parse(inputStream.reader())
            .drop(1) // Dropping the header
            .map {
                validateParameters(it)
                SpeechModel(
                    speaker = it[0],
                    topic = it[1],
                    date = LocalDate.parse(it[2], DateTimeFormatter.ofPattern(CUSTOM_DATE_FORMAT)),
                    words = it[3].toInt()
                )
            }

    private fun validateParameters(csvRecord: CSVRecord?) {
        if (csvRecord == null || csvRecord.size() != 4) {
            throw ParameterSizeMisMatchException()
        }
        csvRecord.toList().forEach {
            if (it == null || it == "") {
                throw ParameterFormatException()
            }
        }
        try {
            LocalDate.parse(csvRecord[2], DateTimeFormatter.ofPattern(CUSTOM_DATE_FORMAT))
            csvRecord[3].toInt()
        } catch (e: Exception) {
            throw ParameterFormatException()
        }
    }

    private fun calculateMostLeastWordy(speeches: ArrayList<SpeechModel>): String? {
        val expressionFun: (SpeechModel) -> Boolean = { true }
        val speakerMap = accumulateSpeaker(speeches, expressionFun)
        val compareFun: (Int, Int) -> Boolean = { count: Int, tempCount: Int -> count < tempCount }
        return compareWordCounts(speakerMap, Int.MAX_VALUE, compareFun)
    }

    private fun calculateMostSecurity(speeches: ArrayList<SpeechModel>): String? {
        val expressionFun: (SpeechModel) -> Boolean = { speechModel -> speechModel.topic == MOST_SECURITY_KEYWORD }
        val speakerMap = accumulateSpeaker(speeches, expressionFun)
        val compareFun: (Int, Int) -> Boolean = { count: Int, tempCount: Int -> count > tempCount }
        return compareWordCounts(speakerMap, Int.MIN_VALUE, compareFun)
    }

    private fun calculateMostSpeeches(speeches: ArrayList<SpeechModel>): String? {
        val expressionFun: (SpeechModel) -> Boolean = { speechModel -> speechModel.date.year == MOST_SPEECH_YEAR }
        val speakerMap = accumulateSpeaker(speeches, expressionFun)
        val compareFun: (Int, Int) -> Boolean = { count: Int, tempCount: Int -> count > tempCount }
        return compareWordCounts(speakerMap, Int.MIN_VALUE, compareFun)
    }

    private fun accumulateSpeaker(
        speeches: ArrayList<SpeechModel>,
        expressionFun: (SpeechModel) -> Boolean
    ): Map<String, Int> {
        val speakerMap = HashMap<String, Int>()
        speeches.forEach {
            if (expressionFun(it)) {
                if (speakerMap[it.speaker] == null) {
                    speakerMap[it.speaker] = it.words
                } else {
                    speakerMap[it.speaker]?.plus(it.words)
                }
            }
        }
        return speakerMap
    }

    private fun compareWordCounts(
        speakerMap: Map<String, Int>,
        initialValue: Int,
        compareFun: (Int, Int) -> Boolean
    ): String? {
        var tempCount = initialValue
        var speaker: String? = null
        speakerMap.forEach {
            if (compareFun(it.value, tempCount)) {
                tempCount = it.value
                speaker = it.key
            } else if (speaker != null && it.value == tempCount) {
                return null
            }
        }
        return speaker
    }


}