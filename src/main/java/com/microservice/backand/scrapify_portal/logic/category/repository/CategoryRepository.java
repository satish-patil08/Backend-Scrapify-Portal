package com.microservice.backand.scrapify_portal.logic.category.repository;

import com.microservice.backand.scrapify_portal.logic.category.entity.Category;
import com.microservice.backand.scrapify_portal.logic.category.repository.customRepo.CategoryCustomRepo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, Long>, CategoryCustomRepo {
}
