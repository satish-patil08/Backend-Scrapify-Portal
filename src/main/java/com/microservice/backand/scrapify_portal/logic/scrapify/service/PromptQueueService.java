package com.microservice.backand.scrapify_portal.logic.scrapify.service;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrappingModel;
import com.microservice.backand.scrapify_portal.logic.scrapify.entity.jobs.JobStatus;
import com.microservice.backand.scrapify_portal.logic.scrapify.entity.jobs.ScrapifyJobs;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyJobStatusResponse;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyJobsListResponse;
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
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PromptQueueService {

    private final Queue<ScrapifyJobs> jobQueue = new ConcurrentLinkedQueue<>();
    private final Queue<ScrapifyJobs> jobsListQueue = new ConcurrentLinkedQueue<>();
    private final AtomicLong jobIdGenerator = new AtomicLong(1);

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
                        jobIdGenerator.getAndIncrement(),
                        model,
                        URLEncoder.encode(replaceVariables(prompt, recordMap), StandardCharsets.UTF_8),
                        category,
                        JobStatus.QUEUED
                ));
            }
        }
        return ResponseEntity.ok(new StatusResponse(
                true,
                "Scraping initiated successfully. " + jobQueue.size() + " prompts generated and queued for processing."));
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

        jobsListQueue.add(new ScrapifyJobs(
                job.getId(),
                job.getModel(),
                job.getFinalPrompt(),
                job.getCategory(),
                JobStatus.RUNNING
        ));
        return new ScrapifyJobStatusResponse(
                true,
                "Job Retrieved Successfully",
                job
        );
    }

    public StatusResponse updateJobStatus(ScrapifyJobs job, JobStatus status) {
        if (job == null) {
            return new StatusResponse(
                    false,
                    "Job is Empty"
            );
        }

        // Remove the job if it already exists in the failedJobs queue
        jobsListQueue.removeIf(j -> j.getId().equals(job.getId()));

        // Add the job with FAILED status
        jobsListQueue.add(new ScrapifyJobs(
                job.getId(),
                job.getModel(),
                job.getFinalPrompt(),
                job.getCategory(),
                status
        ));

        return new StatusResponse(
                true,
                "Job Status Updated."
        );

    }

    public ScrapifyJobsListResponse getQueueList() {
        if (jobQueue.isEmpty()) {
            return new ScrapifyJobsListResponse(
                    false,
                    "Job queue is empty."
            );
        }

        List<ScrapifyJobs> runningJobs = jobsListQueue.stream()
                .map(job -> new ScrapifyJobs(
                        job.getId(),
                        job.getModel(),
                        URLDecoder.decode(job.getFinalPrompt(), StandardCharsets.UTF_8),
                        job.getCategory(),
                        job.getStatus()
                ))
                .toList();

        List<ScrapifyJobs> queuedJobs = jobQueue.stream()
                .map(job -> new ScrapifyJobs(
                        job.getId(),
                        job.getModel(),
                        URLDecoder.decode(job.getFinalPrompt(), StandardCharsets.UTF_8),
                        job.getCategory(),
                        job.getStatus()
                ))
                .toList();

        List<ScrapifyJobs> allJobs = new ArrayList<>();
        allJobs.addAll(runningJobs);
        allJobs.addAll(queuedJobs);

        // Sort List by ASC order
        allJobs.sort(Comparator.comparing(ScrapifyJobs::getId));

        long totalCount = jobQueue.size();
        return new ScrapifyJobsListResponse(
                true,
                totalCount + " jobs pending in the queue.",
                totalCount,
                allJobs
        );
    }

    public StatusResponse clearHistory() {
        if (jobsListQueue.isEmpty()) {
            return new StatusResponse(
                    false,
                    "No History available"
            );
        }

        jobsListQueue.clear();
        return new StatusResponse(
                true,
                "History Cleared Successfully"
        );
    }

    public StatusResponse terminateScrapping() {
        jobsListQueue.clear();
        if (jobQueue.isEmpty()) {
            return new StatusResponse(
                    false,
                    "No Job Queues Available to terminate"
            );
        }

        long queueSize = jobQueue.size();
        jobQueue.clear();
        return new StatusResponse(
                true,
                queueSize + " Jobs terminated successfully."
        );
    }

    public StatusResponse terminateJobById(Long jobId) {
        if (!jobQueue.isEmpty()) {
            for (ScrapifyJobs job : jobQueue) {
                if (job.getId().equals(jobId)) {
                    job.setStatus(JobStatus.TERMINATED);
                    return new StatusResponse(
                            true,
                            "Job terminated successfully.",
                            job
                    );
                }
            }

            // TODO Handle running job termination (scraper and portal service)
            // Check runningQueue for the job
            /*for (ScrapifyJobs job : runningQueue) {
                if (job.getId().equals(jobId)) {
                    job.setStatus(JobStatus.TERMINATED);
                    runningQueue.remove(job); // Remove from running queue
                    return new StatusResponse(
                            true,
                            "Running job terminated successfully.",
                            job
                    );
                }
            }*/
            return new StatusResponse(false, "Job ID not found.");
        }
        return new StatusResponse(
                false,
                "Job Queue Is Empty"
        );
    }

}
