package com.microservice.backand.scrapify_portal.logic.scrapify.controller;

import com.microservice.backand.scrapify_portal.logic.scrapify.service.PromptQueueService;
import com.microservice.backand.scrapify_portal.logic.scrapify.service.ScrapifyService;
import com.microservice.backand.scrapify_portal.modelResponse.ScrapifyJobStatusResponse;
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

    @PostMapping(value = "/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam String prompt,
            @RequestParam Long category
    ) throws Exception {
        return promptQueueService.processCsv(file, prompt, category);
    }

    @GetMapping("/get-jobs")
    public ScrapifyJobStatusResponse getNextJob() {
        return promptQueueService.getJob();
    }
}