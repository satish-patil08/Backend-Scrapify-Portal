package com.microservice.backand.scrapify_portal.logic.users.service;


import com.microservice.backand.scrapify_portal.logic.users.entity.Users;
import com.microservice.backand.scrapify_portal.logic.users.repository.UserRepository;
import com.microservice.backand.scrapify_portal.modelRequest.LoginVerificationRequest;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserCRUDOps {

    @Autowired
    private EncryptingService encryptingService;

    @Autowired
    private UserRepository userRepository;

    public StatusResponse createUser(Users users) {

        if (userRepository.findById(users.getEmail()).isEmpty()) {
            if (users.getPassword() == null && users.getPassword().isEmpty())
                users.setPassword(UUID.randomUUID().toString());

            users.setCreateDateTime(new Date());
            users.setPassword(encryptingService.encrypt(users.getPassword()));

            Users saveUser = userRepository.save(users);
            return new StatusResponse(
                    true,
                    "User Created Successfully",
                    saveUser
            );

        }
        return new StatusResponse(
                false,
                "User with That Email Id Already Exist"
        );

    }

    public StatusResponse resetPassword(LoginVerificationRequest loginVerificationRequest) {
        Optional<Users> user = userRepository.findById(loginVerificationRequest.getEmail());
        if (user.isEmpty()) {
            return new StatusResponse(
                    false,
                    "User With That Email Id Not Exist "
            );
        }

        String encryptedPassword = encryptingService.encrypt(loginVerificationRequest.getPassword());
        if (encryptingService.checkEncryption(loginVerificationRequest.getPassword(), encryptedPassword) && encryptingService.checkEncryption(loginVerificationRequest.getPassword(), user.get().getPassword())) {
            return new StatusResponse(
                    false,
                    "New Password Cannot Be Same As Old Password"
            );
        }
        user.get().setPassword(encryptingService.encrypt(loginVerificationRequest.getPassword()));

        return new StatusResponse(
                true,
                "Password Changed Successfully",
                userRepository.save(user.get())
        );
    }
}
