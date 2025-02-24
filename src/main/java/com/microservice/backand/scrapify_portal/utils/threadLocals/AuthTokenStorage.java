package com.microservice.backand.scrapify_portal.utils.threadLocals;


import static com.microservice.backand.scrapify_portal.constants.Constants.DEFAULT_AUTH_TOKEN;

public class AuthTokenStorage {

    private static final ThreadLocal<String> storage = new ThreadLocal<>();

    public static String getToken() {
        String token = storage.get();
        if (token == null) return DEFAULT_AUTH_TOKEN;
        else return token;
    }

    public static void setToken(final String authToken) {
        storage.set(authToken);
    }

    public static void clear() {
        storage.remove();
    }

}
