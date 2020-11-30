package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.service.ProductService;
import cn.geekcity.xiot.utils.database.config.webclient.WebClientConfiguration;
import cn.geekcity.xiot.utils.database.config.webclient.WebClientFactory;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

public class ProductServiceImpl implements ProductService {

    private final WebClient client;

    public ProductServiceImpl(Vertx vertx, WebClientConfiguration configuration) {
        client = WebClientFactory.create(vertx, configuration);
    }

}
