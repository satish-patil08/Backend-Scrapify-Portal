package com.microservice.backand.scrapify_portal.modelRequest;

public class ScrapifyJobs {
    public String finalPrompt;
    public Long category;

    public ScrapifyJobs() {
    }

    public ScrapifyJobs(String finalPrompt, Long category) {
        this.finalPrompt = finalPrompt;
        this.category = category;
    }

    public String getFinalPrompt() {
        return finalPrompt;
    }

    public void setFinalPrompt(String finalPrompt) {
        this.finalPrompt = finalPrompt;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }


    @Override
    public String toString() {
        return "ScrapifyJobs{" +
                "finalPrompt='" + finalPrompt + '\'' +
                ", category=" + category +
                '}';
    }
}
