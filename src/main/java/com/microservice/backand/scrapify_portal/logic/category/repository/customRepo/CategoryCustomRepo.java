package com.microservice.backand.scrapify_portal.logic.category.repository.customRepo;


import com.microservice.backand.scrapify_portal.logic.category.entity.Category;

import java.util.Optional;

public interface CategoryCustomRepo {
    Optional<Category> existByName(String name);
}
