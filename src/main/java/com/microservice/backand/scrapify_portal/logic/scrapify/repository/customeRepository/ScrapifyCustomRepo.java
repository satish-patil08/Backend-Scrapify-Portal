package com.microservice.backand.scrapify_portal.logic.scrapify.repository.customeRepository;

import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyDataMongoResponse;

public interface ScrapifyCustomRepo {
    ScrapifyDataMongoResponse getContentWithPagination(Long categoryId, Integer page, Integer size);
}
