package org.arif.speechanalyzer.service

import org.arif.speechanalyzer.model.ProcessesCsvFilesResponse

interface EvaluationService {
    fun processesCsvFiles(urlList: List<String>): ProcessesCsvFilesResponse
}