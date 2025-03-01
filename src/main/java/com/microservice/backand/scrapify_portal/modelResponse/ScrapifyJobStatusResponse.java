package com.microservice.backand.scrapify_portal.modelResponse;

import com.microservice.backand.scrapify_portal.modelRequest.ScrapifyJobs;

import java.util.List;

public class ScrapifyJobStatusResponse {
    public Boolean success;
    public String message;
    public List<ScrapifyJobs> data;

    public ScrapifyJobStatusResponse() {
    }

    public ScrapifyJobStatusResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScrapifyJobStatusResponse(Boolean success, String message, List<ScrapifyJobs> data) {
        this.success = success;
        this.message = message;
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

    public List<ScrapifyJobs> getData() {
        return data;
    }

    public void setData(List<ScrapifyJobs> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ScrapifyJobStatusResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
