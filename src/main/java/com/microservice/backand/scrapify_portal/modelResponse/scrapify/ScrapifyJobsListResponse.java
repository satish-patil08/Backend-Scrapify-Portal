package com.microservice.backand.scrapify_portal.modelResponse.scrapify;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.jobs.ScrapifyJobs;

import java.util.List;

public class ScrapifyJobsListResponse {
    public Boolean success;
    public String message;
    public Long totalCount;
    public Long runningCount;
    public Long successCount;
    public Long queuedCount;
    public Long failedCount;
    public Long terminatedCount;
    public Long retryingCount;
    public List<ScrapifyJobs> data;

    public ScrapifyJobsListResponse() {
    }

    public ScrapifyJobsListResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScrapifyJobsListResponse(Boolean success, String message, Long totalCount, Long runningCount, Long successCount, Long queuedCount, Long failedCount, Long terminatedCount, Long retryingCount, List<ScrapifyJobs> data) {
        this.success = success;
        this.message = message;
        this.totalCount = totalCount;
        this.runningCount = runningCount;
        this.successCount = successCount;
        this.queuedCount = queuedCount;
        this.failedCount = failedCount;
        this.terminatedCount = terminatedCount;
        this.retryingCount = retryingCount;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<ScrapifyJobs> getData() {
        return data;
    }

    public void setData(List<ScrapifyJobs> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ScrapifyJobsListResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", totalCount=" + totalCount +
                ", data=" + data +
                '}';
    }
}
