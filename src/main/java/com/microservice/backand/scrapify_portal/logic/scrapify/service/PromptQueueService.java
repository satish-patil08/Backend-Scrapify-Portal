package com.microservice.backand.scrapify_portal.logic.scrapify.service;

import com.microservice.backand.scrapify_portal.modelRequest.ScrapifyJobs;
import com.microservice.backand.scrapify_portal.modelResponse.ScrapifyJobStatusResponse;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PromptQueueService {

    private final Queue<ScrapifyJobs> jobQueue = new ConcurrentLinkedQueue<>();

    public ResponseEntity<Object> processCsv(MultipartFile file, String basePrompt, String urlTemplate, Long category) throws IOException, CsvValidationException {

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] headers = csvReader.readNext();
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Map<String, String> recordMap = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    recordMap.put(headers[i].strip(), row[i].strip());
                }

                String finalPrompt = replaceVariables(basePrompt, recordMap);
                String finalUrl = replaceVariables(urlTemplate, recordMap);
                jobQueue.add(new ScrapifyJobs(
                        URLEncoder.encode(finalPrompt, StandardCharsets.UTF_8),
                        category,
                        URLEncoder.encode(finalUrl, StandardCharsets.UTF_8)
                ));
            }
            System.out.println("GENERATED_PROMPTS----->" + jobQueue);
        }
        return ResponseEntity.ok(new StatusResponse(
                true,
                "File Uploaded Successfully"
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
        List<ScrapifyJobs> jobs = new ArrayList<>(jobQueue);
        if (jobs.isEmpty())
            return new ScrapifyJobStatusResponse(
                    false,
                    "The Job Queue is Empty"
            );
        return new ScrapifyJobStatusResponse(
                true,
                "Job Retrieved Successfully",
                jobs
        );
    }

}
