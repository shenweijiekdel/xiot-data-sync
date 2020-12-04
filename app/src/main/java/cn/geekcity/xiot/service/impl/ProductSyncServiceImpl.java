package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.service.AccountService;
import cn.geekcity.xiot.service.ProductService;
import cn.geekcity.xiot.spec.instance.Device;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductSyncServiceImpl implements ProductService {

    private static final String HOST = "dev-iot-account.knowin.com";
    private static final int PORT = 443;
    private final Logger logger = LoggerFactory.getLogger(ProductSyncServiceImpl.class);
    private final WebClient client;

    public ProductSyncServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setDefaultHost(HOST);
        options.setDefaultPort(PORT);
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    @Override
    public Future<List<Product>> productsWithInstances(String envPrefix) {
        return null;
    }

    @Override
    public Future<Device> getInstance(String envPrefix, String type) {
        return null;
    }
}
