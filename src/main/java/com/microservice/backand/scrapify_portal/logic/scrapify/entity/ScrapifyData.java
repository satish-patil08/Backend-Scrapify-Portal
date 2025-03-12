package com.microservice.backand.scrapify_portal.logic.scrapify.entity;

import com.microservice.backand.scrapify_portal.modelRequest.ChatGPTResponseDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scrapped_data")
public class ScrapifyData {

    @Transient
    public static final String DATA_SCRAPPING_SEQUENCE = "data_scrapping_sequence";

    @Id
    public Long id;
    public String content; // Raw JSON response from ChatGPT
    public Long categoryId;
    public ChatGPTResponseDTO jsonData; // Custom model to store structured data

    public ScrapifyData() {
    }

    public ScrapifyData(Long id, String content, Long categoryId, ChatGPTResponseDTO jsonData) {
        this.id = id;
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

    public ChatGPTResponseDTO getJsonData() {
        return jsonData;
    }

    public void setJsonData(ChatGPTResponseDTO jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return "ScrapifyData{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", categoryId=" + categoryId +
                ", jsonData=" + jsonData +
                '}';
    }
}
