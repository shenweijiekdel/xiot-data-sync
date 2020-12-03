package cn.geekcity.xiot.service;

import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.service.impl.ProductServiceImpl;
import cn.geekcity.xiot.spec.instance.Device;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.List;

public interface ProductService {

    static ProductService create(Vertx vertx) {
        return new ProductServiceImpl(vertx);
    }

    Future<List<Product>> productsWithInstances(String envPrefix);

    Future<Device> getInstance(String envPrefix, String type);
}
