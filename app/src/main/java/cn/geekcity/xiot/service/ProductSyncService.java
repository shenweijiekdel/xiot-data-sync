package cn.geekcity.xiot.service;

import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.VProduct;
import cn.geekcity.xiot.domain.VTemplate;
import cn.geekcity.xiot.service.impl.AccountServiceImpl;
import cn.geekcity.xiot.service.impl.ProductSyncServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public interface ProductSyncService {

    static ProductSyncService create() {
        return new ProductSyncServiceImpl();
    }

    void sync(String currentGroup, VProduct product);
}
