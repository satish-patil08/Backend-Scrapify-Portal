package com.microservice.backand.scrapify_portal.logic.scrapify.controller;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrappingModel;
import com.microservice.backand.scrapify_portal.logic.scrapify.service.PromptQueueService;
import com.microservice.backand.scrapify_portal.logic.scrapify.service.ScrapifyService;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyJobStatusResponse;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyJobsListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/scrapify")
public class ScrapifyController {

    @Autowired
    private PromptQueueService promptQueueService;

    @Autowired
    private ScrapifyService scrapifyService;

    @PostMapping(value = "/initiate-scrapping", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam ScrappingModel model,
            @RequestParam String prompt,
            @RequestParam Long category
    ) throws Exception {
        return promptQueueService.processCsv(file, model, prompt, category);
    }

    @GetMapping("/get-jobs")
    public ScrapifyJobStatusResponse getNextJob() {
        return promptQueueService.getJob();
    }

    @GetMapping("/get-queue-list")
    public ScrapifyJobsListResponse getQueueList() {
        return promptQueueService.getQueueList();
    }

    @GetMapping("/terminate-scrapping")
    public StatusResponse terminateScrapping() {
        return promptQueueService.terminateScrapping();
    }

    @PutMapping("/terminate/job-by-id")
    public StatusResponse terminateJobById(@RequestParam Long jobId) {
        return promptQueueService.terminateJobById(jobId);
    }

    @GetMapping("/get-content-data")
    public ResponseEntity<Object> getContentList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ScrappingModel model,
            @RequestParam(required = false, defaultValue = "false") Boolean exportable,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return scrapifyService.getContentListOrCSV(categoryId, model, exportable, page, size);
    }

}