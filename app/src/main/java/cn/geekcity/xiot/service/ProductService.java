package cn.geekcity.xiot.service;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.service.impl.ProductServiceImpl;
import cn.geekcity.xiot.spec.definition.urn.Urn;
import cn.geekcity.xiot.spec.instance.Device;
import cn.geekcity.xiot.spec.template.DeviceTemplate;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.List;

public interface ProductService {

    static ProductService create(Vertx vertx) {
        return new ProductServiceImpl(vertx);
    }

    Future<List<Product>> productsWithInstances(EnvEnum env);

    Future<Product> getProductWIthInstance(EnvEnum env, String spec, String vendor, String model);

    Future<Device> getInstance(EnvEnum env, String type);

    Future<Product> create(String spec, String model, String name, String template, Group group);

    Future<Void> saveInstance(int productId, int version, Device device, Group group);
}
