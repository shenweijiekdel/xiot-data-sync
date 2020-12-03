package cn.geekcity.xiot.domain;

import cn.geekcity.xiot.spec.definition.urn.DeviceType;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ProductCodec {

    public static Product decode(JsonObject o) {
        Integer id = o.getInteger("id");
        String template = o.getString("template");
        String name = o.getString("name");
        List<Instance> instances = InstanceCodec.decode(o.getJsonArray("instances", new JsonArray()));
        DeviceType type = instances.get(0).getType();
        JsonObject group = o.getJsonObject("group", new JsonObject());
        return new Product(id, name, type.model(), type.ns(), template, group.getString("code")).setInstances(instances);
    }

    public static List<Product> decode(JsonArray arr) {
        List<Product> list = new ArrayList<>();

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
