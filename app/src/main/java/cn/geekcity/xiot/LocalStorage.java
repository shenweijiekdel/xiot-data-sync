package cn.geekcity.xiot;

import java.util.HashMap;
import java.util.Map;

public class LocalStorage {

    private static final Map<String, String> data = new HashMap<>();

    public static String get(String key) {
        return data.get(key);
    }

    public static String put(String key, String value) {
        return data.put(key, value);
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
}
