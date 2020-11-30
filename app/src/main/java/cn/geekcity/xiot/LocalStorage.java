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
}
