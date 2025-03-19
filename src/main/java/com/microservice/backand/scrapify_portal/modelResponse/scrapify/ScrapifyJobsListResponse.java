package com.microservice.backand.scrapify_portal.modelResponse.scrapify;

import com.microservice.backand.scrapify_portal.modelRequest.scrapify.ScrapifyJobs;

import java.util.List;

public class ScrapifyJobsListResponse {
    public Boolean success;
    public String message;
    public Long totalCount;
    public List<ScrapifyJobs> data;

    public ScrapifyJobsListResponse() {
    }

    public ScrapifyJobsListResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScrapifyJobsListResponse(Boolean success, String message, Long totalCount, List<ScrapifyJobs> data) {
        this.success = success;
        this.message = message;
        this.totalCount = totalCount;
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
