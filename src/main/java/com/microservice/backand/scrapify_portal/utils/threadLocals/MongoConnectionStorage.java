package com.microservice.backand.scrapify_portal.utils.threadLocals;

import static com.microservice.backand.scrapify_portal.constants.GlobalConstants.DEFAULT_DB_URL;

public class MongoConnectionStorage {

    private static final ThreadLocal<String> storage = new ThreadLocal<>();


    public static String getConnection() {
        if (storage.get() == null) return DEFAULT_DB_URL;
        return storage.get();
    }

    public static void setConnection(final String connectionString) {
        storage.set(connectionString);
    }

    public static void clear() {
        storage.remove();
        AuthTokenStorage.clear();
    }

}
