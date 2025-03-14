package com.microservice.backand.scrapify_portal.logic.scrapify.service;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrappingModel;
import com.microservice.backand.scrapify_portal.modelRequest.ScrapifyJobs;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyJobStatusResponse;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PromptQueueService {

    private final Queue<ScrapifyJobs> jobQueue = new ConcurrentLinkedQueue<>();

    public ResponseEntity<Object> processCsv(MultipartFile file, ScrappingModel model, String prompt, Long category) throws IOException, CsvValidationException {

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] headers = csvReader.readNext();
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Map<String, String> recordMap = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    recordMap.put(headers[i].strip(), row[i].strip());
                }

                jobQueue.add(new ScrapifyJobs(
                        model,
                        URLEncoder.encode(replaceVariables(prompt, recordMap), StandardCharsets.UTF_8),
                        category
                ));
            }
        }
        return ResponseEntity.ok(new StatusResponse(
                true,
                jobQueue.size() + " Prompts Generated Successfully"
        ));
    }

    private String replaceVariables(String template, Map<String, String> record) {
        String result = template;
        for (Map.Entry<String, String> entry : record.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            result = result.replace(placeholder, entry.getValue());
        }
        return result;
    }

    public ScrapifyJobStatusResponse getJob() {
        ScrapifyJobs job = jobQueue.poll();
        if (job == null)
            return new ScrapifyJobStatusResponse(
                    false,
                    "The Job Queue is Empty"
            );
        return new ScrapifyJobStatusResponse(
                true,
                "Job Retrieved Successfully",
                job
        );
    }

    public StatusResponse getQueueList() {
        if (jobQueue.isEmpty()) {
            return new StatusResponse(
                    false,
                    "Job queue is empty."
            );
        }

        List<ScrapifyJobs> decodedJobs = jobQueue.stream().peek(job -> {
            String decodedPrompt = URLDecoder.decode(job.getFinalPrompt(), StandardCharsets.UTF_8);
            job.setFinalPrompt(decodedPrompt);
        }).toList();

        return new StatusResponse(
                true,
                jobQueue.size() + " jobs pending in the queue.",
                decodedJobs
        );
    }

    public StatusResponse terminateScrapping() {
        if (jobQueue.isEmpty())
            return new StatusResponse(
                    false,
                    "No Job Queues Available to terminate"
            );

        long queueSize = jobQueue.size();
        jobQueue.clear();
        return new StatusResponse(
                true,
                queueSize + " Jobs terminated successfully."
        );
    }

}
