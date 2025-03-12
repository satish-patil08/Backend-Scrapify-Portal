package com.microservice.backand.scrapify_portal.modelResponse.scrapify;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrapifyData;

import java.util.List;

public class ScrapifyDataMongoResponse {
    public Long totalCount;
    public List<ScrapifyData> data;

    public ScrapifyDataMongoResponse() {
    }

    public ScrapifyDataMongoResponse(Long totalCount, List<ScrapifyData> data) {
        this.totalCount = totalCount;
        this.data = data;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<ScrapifyData> getData() {
        return data;
    }

    public void setData(List<ScrapifyData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ScrapifyDataMongoResponse{" +
                "data=" + data +
                ", totalCount=" + totalCount +
                '}';
    }
}
