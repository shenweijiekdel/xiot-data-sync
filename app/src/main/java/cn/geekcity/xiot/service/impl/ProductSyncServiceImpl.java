package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.LocalStorage;
import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.VProduct;
import cn.geekcity.xiot.service.ProductSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;


public class ProductSyncServiceImpl implements ProductSyncService {

    private final Logger logger = LoggerFactory.getLogger(ProductSyncServiceImpl.class);

    @Override
    //TODO 保存失败提示
    public Future<Void> sync(String currentGroup, VProduct product) {
        logger.info("sync product ...");

        if (product.getDiff().isEmpty()) {
            logger.info("no diff skip");
            return;
        }

        EnvEnum env = LocalStorage.getEnv();
        if (env == null) {
            logger.error("env is null");
            return ;
        }

        if (product.getCurrent() == null) {
            Product source = product.getSource();
            Main.productService.create(source.getSpec(),source.getModel(),source.getName(),source.getTemplate())
        }

        Main.productService.save(currentGroup, template.getSource())
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        template.setSource(ar.result());
                    } else {
                        logger.error("sync failed: {}", ar.cause().getMessage());
                    }
                });
    }
}
