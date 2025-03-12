package com.microservice.backand.scrapify_portal.logic.scrapify.repository.customeRepository;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrapifyData;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyDataMongoResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ScrapifyCustomRepoImpl implements ScrapifyCustomRepo {

    private final MongoTemplate mongoTemplate;

    public ScrapifyCustomRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ScrapifyDataMongoResponse getContentWithPagination(Long categoryId, Integer page, Integer size) {
        Query query = new Query();
        ArrayList<Criteria> criteria = new ArrayList<>();

        if (categoryId != null)
            criteria.add(Criteria.where("categoryId").is(categoryId));

        query.addCriteria(criteria.isEmpty() ? new Criteria() : new Criteria().andOperator(criteria));
        long totalCount = mongoTemplate.count(query, ScrapifyData.class);

        if (page != null && size != null)
            query.with(PageRequest.of(page - 1, size));

        List<ScrapifyData> content = mongoTemplate.find(query, ScrapifyData.class);

        return new ScrapifyDataMongoResponse(
                totalCount,
                content
        );
    }
}