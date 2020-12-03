package cn.geekcity.xiot.domain;

import cn.geekcity.xiot.spec.definition.urn.DeviceType;
import cn.geekcity.xiot.spec.lifecycle.Lifecycle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupCodec {

    public static Group decode(JsonObject o) {
        int id = o.getInteger("id", 0);
        String code = o.getString("code");
        JsonObject description = o.getJsonObject("description");
        Group group = new Group();
        group.setCode(code);
        group.setId(id);
        Map<String, String> descMap = new HashMap<>();
        description.getMap().forEach((k, v) -> {
            descMap.put(k, v.toString());
        });

        group.setDescription(descMap);
        return group;
    }

    public static List<Group> decode(JsonArray arr) {
        List<Group> list = new ArrayList<>();

        if (arr != null) {
            for (Object o : arr) {
                if (o instanceof JsonObject) {
                    list.add(decode((JsonObject) o));
                }
            }
        }

        return list;
    }
}
