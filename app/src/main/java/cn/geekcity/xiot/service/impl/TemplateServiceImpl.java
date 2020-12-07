package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.LocalStorage;
import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.ProductCodec;
import cn.geekcity.xiot.service.ProductService;
import cn.geekcity.xiot.service.TemplateService;
import cn.geekcity.xiot.service.TemplateSyncService;
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

public class TemplateServiceImpl implements TemplateService {

    private static final String CONSOLE_HOST_SUFFIX = "iot-dvlp.knowin.com";
    private static final String SPEC_hHOST_SUFFIX = "iot-spec.knowin.com";
    private final WebClient client;
    private final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

    public TemplateServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    @Override
    public Future<DeviceTemplate> save(String currentGroup, DeviceTemplate template) {
        Promise<DeviceTemplate> promise = Promise.promise();

        EnvEnum env = LocalStorage.getEnv();
        if (env == null) {
            promise.fail("env not found");
            return promise.future();
        }

        getTemplate(env, template.type())
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        update(env, currentGroup, template)
                                .compose(x -> getTemplate(env, template.type()))
                                .onComplete(promise);
                    } else {
                        logger.info("create template");
                        create(env, currentGroup, template)
                                .compose(x -> getTemplate(env, template.type()))
                                .onComplete(promise);
                    }
                });

        return promise.future();
    }

    private Future<Void> create(EnvEnum env, String currentGroup, DeviceTemplate template) {
        Promise<Void> promise = Promise.promise();

        client.postAbs(String.format("https://%s%s/template/item/%s", env.getPrefix(), CONSOLE_HOST_SUFFIX, template.type().toString()))
                .putHeader("current-group", currentGroup)
                .bearerTokenAuthentication(LocalStorage.getToken())
                .sendJsonObject(new JsonObject().put("content", DeviceTemplateCodec.encode(template)), ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                promise.complete();
                            } else {
                                String description = body.getString("description");
                                logger.error("create template error: {}", description);
                                promise.fail("create template error: " + description);
                            }
                        } else {
                            logger.error("create template error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("create template error: {}", ar.cause().getMessage());
                        promise.fail("create template error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }

    private Future<Void> update(EnvEnum env, String currentGroup, DeviceTemplate template) {
        Promise<Void> promise = Promise.promise();

        client.putAbs(String.format("https://%s%s/template/item/%s", env.getPrefix(), CONSOLE_HOST_SUFFIX, template.type().toString()))
                .putHeader("current-group", currentGroup)
                .bearerTokenAuthentication(LocalStorage.getToken())
                .sendJsonObject(new JsonObject().put("content", DeviceTemplateCodec.encode(template)), ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                promise.complete();
                            } else {
                                String description = body.getString("description");
                                logger.error("update template error: {}", description);
                                promise.fail("update template error: " + description);
                            }
                        } else {
                            logger.error("update template error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("update template error: {}", ar.cause().getMessage());
                        promise.fail("update template error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }

    @Override
    public Future<DeviceTemplate> getTemplate(EnvEnum env, Urn type) {
        Promise<DeviceTemplate> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/template?type=%s", env.getPrefix(), SPEC_hHOST_SUFFIX, type.toString()))
                .send(ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());
                                promise.complete(DeviceTemplateCodec.decode(data));
                            } else {
                                String description = body.getString("description");
                                logger.error("get template error: {}", description);
                                promise.fail("get template error: " + description);
                            }
                        } else {
                            logger.error("get template error: status={}", ar.result().statusCode());
                            promise.fail(ar.result().statusMessage());
                        }
                    } else {
                        logger.error("get template error: {}", ar.cause());
                        promise.fail("get template error: " + ar.cause().getMessage());
                    }
                });

        return promise.future();
    }


    @Override
    public Future<List<DeviceTemplate>> getTemplates(EnvEnum env) {
        return templateTypes(env)
                .compose(x -> FutureMerger.merge(x.stream()
                        .map(x1 -> getTemplate(env, x1))
                        .collect(Collectors.toList())));
    }

    private Future<List<DeviceType>> templateTypes(EnvEnum env) {
        Promise<List<DeviceType>> promise = Promise.promise();

        client.getAbs(String.format("https://%s%s/templates", env.getPrefix(), SPEC_hHOST_SUFFIX))
                .send(ar -> {
                    if (ar.succeeded()) {
                        if (ar.result().statusCode() == 200) {
                            JsonObject body = ar.result().bodyAsJsonObject();
                            if (body.getString("msg").equals("ok")) {
                                JsonObject data = body.getJsonObject("data", new JsonObject());

                                promise.complete(data.getJsonArray("templates").
                                        stream().map(x -> (JsonObject) x)
                                        .map(x -> DeviceType.fromString(x.getString("type")))
                                        .collect(Collectors.toList()));
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
}
