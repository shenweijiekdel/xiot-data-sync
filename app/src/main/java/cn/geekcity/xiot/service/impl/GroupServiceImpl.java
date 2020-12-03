package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.GroupCodec;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.ProductCodec;
import cn.geekcity.xiot.service.GroupService;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GroupServiceImpl implements GroupService {

    private static final String HOST_SUFFIX = "iot-dvlp.knowin.com";
    private final WebClient client;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public GroupServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    @Override
    public Future<List<Group>> available(String targetEnvPrefix) {
        Promise<List<Group>> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/group/available", targetEnvPrefix, HOST_SUFFIX))
                .send(ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());

                                promise.complete(GroupCodec.decode(data.getJsonArray("products")));
                            } else {
                                String description = body.getString("description");
                                logger.error("get group error: {}", description);
                                promise.fail("get group error: " + description);
                            }
                        } else {
                            logger.error("get group error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("get group error: {}", ar.cause());
                        promise.fail("get group error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }
}
