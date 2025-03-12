package com.microservice.backand.scrapify_portal.modelResponse.scrapify;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrapifyData;

import java.util.List;

public class ScrapifyContentListResponse {
    public Boolean success;
    public String message;
    public Long totalCount;
    public List<ScrapifyData> contentList;

    public ScrapifyContentListResponse() {
    }

    public ScrapifyContentListResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScrapifyContentListResponse(Boolean success, String message, Long totalCount, List<ScrapifyData> contentList) {
        this.success = success;
        this.message = message;
        this.totalCount = totalCount;
        this.contentList = contentList;
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

    public List<ScrapifyData> getContentList() {
        return contentList;
    }

    public void setContentList(List<ScrapifyData> contentList) {
        this.contentList = contentList;
    }

    @Override
    public String toString() {
        return "ScrapifyContentListResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", totalCount=" + totalCount +
                ", contentList=" + contentList +
                '}';
    }
}
