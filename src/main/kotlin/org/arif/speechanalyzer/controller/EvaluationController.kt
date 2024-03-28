package org.arif.speechanalyzer.controller

import org.arif.speechanalyzer.model.ProcessesCsvFilesResponse
import org.arif.speechanalyzer.service.EvaluationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/evaluation")
class EvaluationController(val evaluationService: EvaluationService) {

    @PostMapping
    fun processesCsvFiles(@RequestParam(value = "url") urlList: List<String>): ProcessesCsvFilesResponse {
        return evaluationService.processesCsvFiles(urlList)
    }
}