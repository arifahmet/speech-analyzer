package org.arif.speechanalyzer.service.impl

import org.arif.speechanalyzer.exception.ErrorMessageEnum
import org.arif.speechanalyzer.exception.ParameterFormatException
import org.arif.speechanalyzer.exception.ParameterSizeMisMatchException
import org.arif.speechanalyzer.service.EvaluationService
import org.assertj.core.api.AssertionsForClassTypes.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import kotlin.test.assertFailsWith

class EvaluationServiceImplTest {

    @Mock
    private val evaluationServiceImpl: EvaluationService = EvaluationServiceImpl();

    @Test
    fun processesCsvFilesSuccessfully() {
        //GIVEN
        val resource = ClassLoader.getSystemResource("SpeechesSuccess.csv");
        val urlList = ArrayList<String>()
        urlList.add(resource.toURI().path)
        //THEN
        val response = evaluationServiceImpl.processesCsvFiles(urlList)
        assertThat(response.mostSpeeches).isNull()
        assertThat(response.mostSecurity).isEqualTo("Alexander Abel")
        assertThat(response.leastWordy).isEqualTo("Caesare Collins")
    }

    @Test
    fun processesCsvFilesFailWhenParamSizeMismatch() {
        //GIVEN
        val resource = ClassLoader.getSystemResource("SpeechesWrongParamSize.csv");
        val urlList = ArrayList<String>()
        urlList.add(resource.toURI().path)
        //THEN
        val exception = assertFailsWith<ParameterSizeMisMatchException>(
            block = { evaluationServiceImpl.processesCsvFiles(urlList) }
        )
        assertThat(exception.errorMessage).isEqualTo(ErrorMessageEnum.PARAMETER_SIZE_MISMATCH.message)
    }

    @ParameterizedTest
    @ValueSource(strings = ["SpeechesWrongIntegerFormat.csv", "SpeechesWrongDateFormat.csv", "SpeechesParameterMissing.csv"])
    fun processesCsvFilesThrowParameterFormatException(fileName: String) {
        //GIVEN
        val resource = ClassLoader.getSystemResource(fileName);
        val urlList = ArrayList<String>()
        urlList.add(resource.toURI().path)
        //THEN
        val exception = assertFailsWith<ParameterFormatException>(
            block = { evaluationServiceImpl.processesCsvFiles(urlList) }
        )
        assertThat(exception.errorMessage).isEqualTo(ErrorMessageEnum.PARAMETER_FORMAT_NOT_VALID.message)
    }
}