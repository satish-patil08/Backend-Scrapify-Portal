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

    public ResponseEntity<Object> processCsv(MultipartFile file, ScrappingModel model, String prompt, Long category, LinkedList<String> fields) throws IOException, CsvValidationException {

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] headers = csvReader.readNext();
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Map<String, String> recordMap = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    recordMap.put(headers[i].strip(), row[i].strip());
                }

                ScrapifyJobs jobs = new ScrapifyJobs(
                        jobIdGenerator.getAndIncrement(),
                        model,
                        URLEncoder.encode(replaceVariables(prompt, recordMap), StandardCharsets.UTF_8),
                        category,
                        JobStatus.QUEUED,
                        fields
                );

                jobQueue.add(jobs);
                jobsListQueue.add(jobs);
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

        jobsListQueue.removeIf(j -> j.getId().equals(job.getId()));

        jobsListQueue.add(new ScrapifyJobs(
                job.getId(),
                job.getModel(),
                job.getFinalPrompt(),
                job.getCategory(),
                JobStatus.RUNNING,
                job.getFields()
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

        jobsListQueue.add(new ScrapifyJobs(
                job.getId(),
                job.getModel(),
                job.getFinalPrompt(),
                job.getCategory(),
                status,
                job.getFields()
        ));

        return new StatusResponse(
                true,
                "Job Status Updated."
        );

    }

    public ScrapifyJobsListResponse getQueueList() {
        if (jobsListQueue.isEmpty()) {
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
                        job.getStatus(),
                        job.getFields()
                ))
                .toList();

        List<ScrapifyJobs> allJobs = new ArrayList<>(runningJobs);

        // Count jobs based on their statuses
        long runningCount = allJobs.stream().filter(job -> job.getStatus() == JobStatus.RUNNING).count();
        long successCount = allJobs.stream().filter(job -> job.getStatus() == JobStatus.SUCCESS).count();
        long queuedCount = allJobs.stream().filter(job -> job.getStatus() == JobStatus.QUEUED).count();
        long failedCount = allJobs.stream().filter(job -> job.getStatus() == JobStatus.FAILED).count();
        long terminatedCount = allJobs.stream().filter(job -> job.getStatus() == JobStatus.TERMINATED).count();
        long retryingCount = allJobs.stream().filter(job -> job.getStatus() == JobStatus.RETRYING).count();

        // Sort List by ASC order
        allJobs.sort(Comparator.comparing(ScrapifyJobs::getId));

        long totalCount = allJobs.size();
        return new ScrapifyJobsListResponse(
                true,
                "Job list retrieved successfully",
                totalCount,
                runningCount,
                successCount,
                queuedCount,
                failedCount,
                terminatedCount,
                retryingCount,
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
        if (jobQueue.isEmpty()) {
            return new StatusResponse(
                    false,
                    "No Job Queues Available to terminate"
            );
        }

        long queueSize = jobQueue.size();
        while (!jobQueue.isEmpty()) {
            ScrapifyJobs job = jobQueue.poll();
            updateJobStatus(job, JobStatus.TERMINATED);
        }

        return new StatusResponse(
                true,
                queueSize + " Jobs terminated successfully."
        );
    }

    public StatusResponse terminateJobById(Long jobId) {
        if (jobQueue.isEmpty()) return new StatusResponse(
                false,
                "Job Queue is Empty"
        );

        for (ScrapifyJobs job : jobQueue) {
            if (job.getId().equals(jobId)) {
                if (job.getStatus() == JobStatus.RUNNING) {
                    return new StatusResponse(
                            false,
                            "Running Job Cannot Be Terminated"
                    );
                }
                job.setStatus(JobStatus.TERMINATED);
                updateJobStatus(job, JobStatus.TERMINATED);
                return new StatusResponse(
                        true,
                        "Job terminated successfully."
                );
            }
        }
        return new StatusResponse(
                false,
                "Job not found in the queue."
        );
    }

}
