package org.arif.speechanalyzer.service

import org.arif.speechanalyzer.model.ProcessesCsvFilesResponse
import org.springframework.http.ResponseEntity

interface EvaluationService {
    fun processesCsvFiles(urlList: List<String>): ProcessesCsvFilesResponse
}