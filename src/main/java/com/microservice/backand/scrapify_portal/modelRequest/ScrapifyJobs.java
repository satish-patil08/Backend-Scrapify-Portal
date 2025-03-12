package com.microservice.backand.scrapify_portal.modelRequest;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrappingModel;

public class ScrapifyJobs {
    public ScrappingModel model;
    public String finalPrompt;
    public Long category;

    public ScrapifyJobs() {
    }

    public ScrapifyJobs(ScrappingModel model, String finalPrompt, Long category) {
        this.model = model;
        this.finalPrompt = finalPrompt;
        this.category = category;
    }

    public ScrappingModel getModel() {
        return model;
    }

    public void setModel(ScrappingModel model) {
        this.model = model;
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
                "model=" + model +
                ", finalPrompt='" + finalPrompt + '\'' +
                ", category=" + category +
                '}';
    }
}
