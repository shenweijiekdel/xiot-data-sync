package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.ProductCodec;
import cn.geekcity.xiot.service.ProductService;
import cn.geekcity.xiot.spec.codec.vertx.instance.DeviceCodec;
import cn.geekcity.xiot.spec.instance.Device;
import cn.geekcity.xiot.utils.future.FutureMerger;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private static final String HOST_SUFFIX = "iot-spec.knowin.com";
    private final WebClient client;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    private Future<List<Product>> products(String envPrefix) {
        Promise<List<Product>> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/products", envPrefix, HOST_SUFFIX))
                .send(ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());

                                promise.complete(ProductCodec.decode(data.getJsonArray("products")));
                            } else {
                                String description = body.getString("description");
                                logger.error("get product error: {}", description);
                                promise.fail("get product error: " + description);
                            }
                        } else {
                            logger.error("get product error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("get product error: {}", ar.cause());
                        promise.fail("get product error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }

    @Override
    public Future<List<Product>> productsWithInstances(String envPrefix) {
        return products(envPrefix)
                .compose(x -> FutureMerger.merge(x.stream().map(x1 -> addInstances(envPrefix, x1)).collect(Collectors.toList())));
    }

    @Override
    public Future<Device> getInstance(String envPrefix, String type) {
        Promise<Device> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/instance?type=%s", envPrefix, HOST_SUFFIX, type))
                .send(ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());

                                promise.complete(DeviceCodec.decode(data));
                            } else {
                                String description = body.getString("description");
                                logger.error("get instance error: {}", description);
                                promise.fail("get instance error: " + description);
                            }
                        } else {
                            logger.error("get instance error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("get instance error: {}", ar.cause());
                        promise.fail("get instance error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }

    private Future<Product> addInstances(String envPrefix, Product product) {
        Promise<Product> promise = Promise.promise();

        FutureMerger.merge(product.getInstances().values().stream().map(x -> addInstances(envPrefix, x)).collect(Collectors.toList()))
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        product.setInstances(ar.result());
                        promise.complete(product);
                    } else {
                        ar.cause().printStackTrace();
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }


    private Future<Instance> addInstances(String envPrefix, Instance instance) {
        Promise<Instance> promise = Promise.promise();

        getInstance(envPrefix, instance.getType().toString())
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        instance.setContent(ar.result());
                        promise.complete(instance);
                    } else {
                        ar.cause().printStackTrace();
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }

}
