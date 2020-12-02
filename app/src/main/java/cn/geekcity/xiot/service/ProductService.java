package cn.geekcity.xiot.service;

import cn.geekcity.xiot.service.impl.ProductServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;

public interface ProductService {

    static ProductService create(Vertx vertx) {
        return new ProductServiceImpl(vertx);
    }

    Future<JsonArray> products(String envPrefix);
}
