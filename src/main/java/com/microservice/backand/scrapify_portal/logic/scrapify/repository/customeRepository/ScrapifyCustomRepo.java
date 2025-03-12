package com.microservice.backand.scrapify_portal.logic.scrapify.repository.customeRepository;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrappingModel;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyDataMongoResponse;

public interface ScrapifyCustomRepo {
    ScrapifyDataMongoResponse getContentWithPagination(Long categoryId, ScrappingModel model, Integer page, Integer size);
}
