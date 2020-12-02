package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.service.ProductService;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductServiceImpl implements ProductService {

    private static final String HOST_SUFFIX = "iot-spec.knowin.com";
    private final WebClient client;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    @Override
    public Future<JsonArray> products(String envPrefix) {
        Promise<JsonArray> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/products", envPrefix, HOST_SUFFIX))
                .send(ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());

                                promise.complete(data.getJsonArray("products"));
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
