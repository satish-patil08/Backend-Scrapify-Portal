package com.microservice.backand.scrapify_portal.logic.scrapify.entity.jobs;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrappingModel;

import java.util.LinkedList;

public class ScrapifyJobs {
    public Long id;
    public ScrappingModel model;
    public String finalPrompt;
    public Long category;
    public JobStatus status;
    public LinkedList<String> fields;

    public ScrapifyJobs() {
    }

    public ScrapifyJobs(Long id, ScrappingModel model, String finalPrompt, Long category, JobStatus status, LinkedList<String> fields) {
        this.id = id;
        this.model = model;
        this.finalPrompt = finalPrompt;
        this.category = category;
        this.status = status;
        this.fields = fields;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LinkedList<String> getFields() {
        return fields;
    }

    public void setFields(LinkedList<String> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "ScrapifyJobs{" +
                "id=" + id +
                ", model=" + model +
                ", finalPrompt='" + finalPrompt + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", fields=" + fields +
                '}';
    }
}
