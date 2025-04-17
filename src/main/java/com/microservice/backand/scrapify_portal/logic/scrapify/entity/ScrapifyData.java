package com.microservice.backand.scrapify_portal.logic.scrapify.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "scrapped_data")
public class ScrapifyData {

    @Transient
    public static final String DATA_SCRAPPING_SEQUENCE = "data_scrapping_sequence";

    @Id
    public Long id;
    public ScrappingModel model;
    public String content; // Raw JSON response from ChatGPT
    public Long categoryId;
    public HashMap<String, Object> jsonData;

    public ScrapifyData() {
    }

    public ScrapifyData(Long id, ScrappingModel model, String content, Long categoryId, HashMap<String, Object> jsonData) {
        this.id = id;
        this.model = model;
        this.content = content;
        this.categoryId = categoryId;
        this.jsonData = jsonData;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public HashMap<String, Object> getJsonData() {
        return jsonData;
    }

    public void setJsonData(HashMap<String, Object> jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return "ScrapifyData{" +
                "id=" + id +
                ", model=" + model +
                ", content='" + content + '\'' +
                ", categoryId=" + categoryId +
                ", jsonData=" + jsonData +
                '}';
    }
}
