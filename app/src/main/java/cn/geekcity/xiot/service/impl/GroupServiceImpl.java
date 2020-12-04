package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.LocalStorage;
import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.GroupCodec;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.ProductCodec;
import cn.geekcity.xiot.service.GroupService;
import cn.geekcity.xiot.service.ProductSyncService;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GroupServiceImpl implements ProductSyncService {

    private static final String HOST_SUFFIX = "iot-dvlp.knowin.com";
    private final WebClient client;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public GroupServiceImpl(Vertx vertx) {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        client = WebClient.create(vertx, options);
    }

    @Override
    public Future<JsonObject> sync(String targetEnvPrefix, Product product) {
        return null;
    }
}
