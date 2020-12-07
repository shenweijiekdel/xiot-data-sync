package cn.geekcity.xiot.service;

import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.VProduct;
import cn.geekcity.xiot.service.impl.ProductSyncServiceImpl;
import io.vertx.core.Future;

public interface ProductSyncService {

    static ProductSyncService create() {
        return new ProductSyncServiceImpl();
    }

    Future<Product> sync(Group group, VProduct product);
}
