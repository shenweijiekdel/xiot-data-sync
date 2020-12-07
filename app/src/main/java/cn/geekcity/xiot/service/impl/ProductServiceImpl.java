package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.LocalStorage;
import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.ProductCodec;
import cn.geekcity.xiot.service.ProductService;
import cn.geekcity.xiot.spec.codec.vertx.instance.DeviceCodec;
import cn.geekcity.xiot.spec.codec.vertx.template.DeviceTemplateCodec;
import cn.geekcity.xiot.spec.definition.urn.DeviceType;
import cn.geekcity.xiot.spec.definition.urn.Urn;
import cn.geekcity.xiot.spec.instance.Device;
import cn.geekcity.xiot.spec.template.DeviceTemplate;
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

    private static final String CONSOLE_HOST_SUFFIX = "iot-dvlp.knowin.com";
    private static final String SPEC_HOST_SUFFIX = "iot-spec.knowin.com";
    private final WebClient client;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    @Override
    public Future<List<Product>> productsWithInstances(EnvEnum env) {
        return products(env)
                .compose(x -> FutureMerger.merge(x.stream().map(x1 -> addInstances(env, x1)).collect(Collectors.toList())));
    }

    @Override
    public Future<Product> getProductWIthInstance(EnvEnum env, String spec, String vendor, String model) {
        return getProduct(env, spec, vendor, model)
                .compose(x -> addInstances(env, x));
    }

    @Override
    public Future<Device> getInstance(EnvEnum env, String type) {
        Promise<Device> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/instance?type=%s", env.getPrefix(), SPEC_HOST_SUFFIX, type))
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

    @Override
    public Future<Product> create(String spec, String model, String name, String template, Group group) {
        Promise<Product> promise = Promise.promise();


        EnvEnum env = LocalStorage.getEnv();
        if (env == null) {
            promise.fail("env not found");
            return promise.future();
        }

        client.postAbs(String.format("https://%s%s/product/new", env.getPrefix(), CONSOLE_HOST_SUFFIX))
                .bearerTokenAuthentication(LocalStorage.getToken())
                .putHeader("current-group", String.valueOf(group.getId()))
                .sendJsonObject(new JsonObject().put("icon", "").put("name", name).put("model", model).put("spec", spec).put("template", template), ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                getProduct(env, spec, group.getCode(), model).onComplete(promise);
                            } else {
                                String description = body.getString("description");
                                logger.error("save product error: {}", description);
                                promise.fail("save product error: " + description);
                            }
                        } else {
                            logger.error("save product error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("save product error: {}", ar.cause());
                        promise.fail("save product error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }

    private Future<Product> addInstances(EnvEnum env, Product product) {
        Promise<Product> promise = Promise.promise();

        FutureMerger.merge(product.getInstances().values().stream().map(x -> addInstances(env, x)).collect(Collectors.toList()))
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


    private Future<Instance> addInstances(EnvEnum env, Instance instance) {
        Promise<Instance> promise = Promise.promise();

        getInstance(env, instance.getType().toString())
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

    private Future<List<Product>> products(EnvEnum env) {
        Promise<List<Product>> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/products", env.getPrefix(), SPEC_HOST_SUFFIX))
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
    public Future<Void> saveInstance(int productId, int version, Device device, Group group) {
        Promise<Void> promise = Promise.promise();

        EnvEnum env = LocalStorage.getEnv();
        if (env == null) {
            promise.fail("env not found");
            return promise.future();
        }

        client.putAbs(String.format("https://%s%s/instance/product/%s/%s", env.getPrefix(), CONSOLE_HOST_SUFFIX, productId, version))
                .bearerTokenAuthentication(LocalStorage.getToken())
                .putHeader("current-group", String.valueOf(group.getId()))
                .sendJsonObject(new JsonObject().put("content", DeviceCodec.encode(device)), ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                promise.complete();
                            } else {
                                String description = body.getString("description");
                                logger.error("save instance error: {}", description);
                                promise.fail("save instance error: " + description);
                            }
                        } else {
                            logger.error("save instance error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("save instance error: {}", ar.cause());
                        promise.fail("save instance error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }

    private Future<Product> getProduct(EnvEnum env, String spec, String vendor, String model) {
        Promise<Product> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/product?spec=%s&vendor=%s&model=%s", env.getPrefix(), SPEC_HOST_SUFFIX, spec, vendor, model))
                .send(ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());

                                promise.complete(ProductCodec.decode(data));
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
}
