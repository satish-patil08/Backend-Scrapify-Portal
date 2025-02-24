package com.microservice.backand.scrapify_portal.logic.users.controller;


import com.microservice.backand.scrapify_portal.logic.users.entity.Users;
import com.microservice.backand.scrapify_portal.logic.users.service.UserCRUDOps;
import com.microservice.backand.scrapify_portal.logic.users.service.UserLoginService;
import com.microservice.backand.scrapify_portal.modelRequest.LoginVerificationRequest;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserCRUDOps userCRUDOps;

    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/register")
    public StatusResponse createUser(@RequestBody Users users) {
        return userCRUDOps.createUser(users);
    }

    @PostMapping("/login")
    public StatusResponse loginUser(@RequestBody LoginVerificationRequest loginVerificationRequest) {
        return userLoginService.loginUser(loginVerificationRequest);
    }

}
