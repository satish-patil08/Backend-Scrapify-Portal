package com.microservice.backand.scrapify_portal.logic.users.service;


import com.microservice.backand.scrapify_portal.logic.users.entity.Users;
import com.microservice.backand.scrapify_portal.logic.users.repository.UserRepository;
import com.microservice.backand.scrapify_portal.modelRequest.LoginVerificationRequest;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import com.microservice.backand.scrapify_portal.utils.jwtUtils.CustomUserDetails;
import com.microservice.backand.scrapify_portal.utils.jwtUtils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptingService encryptingService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;


    public StatusResponse loginUser(LoginVerificationRequest loginVerificationRequest) {
        Optional<Users> users = userRepository.findById(loginVerificationRequest.getEmail());
        if (users.isEmpty()) {
            return new StatusResponse(
                    false,
                    "Incorrect Username"
            );
        } else if (!encryptingService.checkEncryption(loginVerificationRequest.getPassword(), users.get().getPassword())) {
            return new StatusResponse(
                    false,
                    "Incorrect Password"
            );
        }
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginVerificationRequest.getEmail());
        final String jwt = jwtUtils.generateToken(
                new CustomUserDetails(
                        userDetails.getUsername(),
                        userDetails.getPassword()
                ),
                new Date(new Date().getTime() + 1000 * 60 * 60 * 24)
        );
        users.get().setAuthToken(jwt);
        users.get().setPassword(null);

        return new StatusResponse(
                true,
                "Login Request Succeeded",
                users
        );
    }
}
