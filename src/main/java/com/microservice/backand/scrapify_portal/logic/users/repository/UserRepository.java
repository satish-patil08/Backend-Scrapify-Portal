package com.microservice.backand.scrapify_portal.logic.users.repository;

import com.microservice.backand.scrapify_portal.logic.users.entity.Users;
import com.microservice.backand.scrapify_portal.logic.users.repository.customRepo.UsersCustomRepo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String>, UsersCustomRepo {
}
