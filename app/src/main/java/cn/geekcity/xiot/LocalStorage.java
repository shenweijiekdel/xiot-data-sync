package cn.geekcity.xiot;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class LocalStorage {

    private static final Map<String, String> data = new HashMap<>();

    public static String get(String key) {
        return data.get(key);
    }

    public static void put(String key, String value) {
        data.put(key, value);
    }

    public static String getToken() {
        return data.get("token");
    }

    public static String setToken(String token) {
        return data.put("token", token);
    }

    public static String getCurrentGroup() {
        return data.get("current-group");
    }

    public static String setCurrentGroup(String groupId) {
        return data.put("current-group", groupId);
    }

    public static void setEnv(EnvEnum env) {
        data.put("environment", env.name());
    }

    public static EnvEnum getEnv() {
        String environment = data.get("environment");
        if (environment == null) {
            return null;
        }

        return EnvEnum.valueOf(environment);
    }
}
