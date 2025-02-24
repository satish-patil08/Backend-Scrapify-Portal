package com.microservice.backand.scrapify_portal.modelRequest;

public class PromptRequest {
    public Long categoryId;
    public String url;
    public String prompt;

    public PromptRequest() {
    }

    public PromptRequest(Long categoryId, String url, String prompt) {
        this.categoryId = categoryId;
        this.url = url;
        this.prompt = prompt;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        return "PromptRequest{" +
                "categoryId=" + categoryId +
                ", url='" + url + '\'' +
                ", prompt='" + prompt + '\'' +
                '}';
    }
}
