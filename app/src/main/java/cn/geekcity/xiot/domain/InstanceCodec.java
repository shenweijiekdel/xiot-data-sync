package cn.geekcity.xiot.domain;

import cn.geekcity.xiot.spec.definition.urn.DeviceType;
import cn.geekcity.xiot.spec.lifecycle.Lifecycle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class InstanceCodec {

    public static Instance decode(JsonObject o) {
        int version = o.getInteger("version", 1);
        Lifecycle lifecycle = Lifecycle.fromString(o.getString("status"));
        String type = o.getString("type");
        return new Instance(version, lifecycle, DeviceType.fromString(type), null);
    }

    public static List<Instance> decode(JsonArray arr) {
        List<Instance> list = new ArrayList<>();

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
