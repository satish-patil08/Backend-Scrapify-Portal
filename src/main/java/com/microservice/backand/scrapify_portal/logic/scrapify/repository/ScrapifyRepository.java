package com.microservice.backand.scrapify_portal.logic.scrapify.repository;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrapifyData;
import com.microservice.backand.scrapify_portal.logic.scrapify.repository.customeRepository.ScrapifyCustomRepo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScrapifyRepository extends MongoRepository<ScrapifyData, Long>, ScrapifyCustomRepo {
}
