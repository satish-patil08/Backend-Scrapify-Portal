package com.microservice.backand.scrapify_portal.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.backand.scrapify_portal.utils.threadLocals.MongoConnectionStorage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ClientRequestFilter extends OncePerRequestFilter {

    // Assuming you have this defined at the class level
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bypass wrapping for multipart requests
        if (request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart/")) {
            filterChain.doFilter(request, response);
            return;
        }

        ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(request);
        String path = request.getServletPath();

        if (path.equals("/users/login") || path.equals("/users/register")) {
            filterChain.doFilter(wrappedRequest, response);
            return;
        }

        filterChain.doFilter(wrappedRequest, response);
        MongoConnectionStorage.clear();
    }
}
