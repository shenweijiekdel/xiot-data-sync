package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.service.AccountService;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountServiceImpl implements AccountService {

    private static final String HOST_SUFFIX = "iot-account.knowin.com";
    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final WebClient client;

    public AccountServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    @Override
    public Future<JsonObject> login(EnvEnum env, String username, String password) {
        Promise<JsonObject> promise = Promise.promise();

        client.postAbs(String.format("https://%s%s/v1/login", env.getPrefix(), HOST_SUFFIX))
                .sendJson(new JsonObject().put("username", username).put("password", password), ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());
                                promise.complete(data);
                            } else {
                                String description = body.getString("description");
                                logger.error("login error: {}", description);
                                promise.fail("login error: " + description);
                            }
                        } else {
                            logger.error("login error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("login error: {}", ar.cause());
                        promise.fail("login error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }
}
