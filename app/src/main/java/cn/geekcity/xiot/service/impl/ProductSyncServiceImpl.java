package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.LocalStorage;
import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.VProduct;
import cn.geekcity.xiot.service.ProductSyncService;
import cn.geekcity.xiot.spec.definition.urn.DeviceType;
import cn.geekcity.xiot.utils.future.FutureMerger;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ProductSyncServiceImpl implements ProductSyncService {

    private final Logger logger = LoggerFactory.getLogger(ProductSyncServiceImpl.class);

    @Override
    //TODO 保存失败提示
    public Future<Product> sync(Group group, VProduct product) {
        Promise<Product> promise = Promise.promise();

        logger.info("sync product ...");

        if (product.getDiff().isEmpty()) {
            logger.info("no diff skip");
            return promise.future();
        }

        EnvEnum env = LocalStorage.getEnv();
        if (env == null) {
            logger.error("env is null");
            return promise.future();
        }

        Main.templateService.getTemplate(env, DeviceType.fromString(product.getTemplate()))
                .onComplete(tar -> {
                    if (tar.succeeded()) {
                        if (product.getCurrent() == null) {
                            Product source = product.getSource();
                            Main.productService.create(source.getSpec(), source.getModel(), source.getName(), source.getTemplate(), group)
                                    .compose(x -> saveInstances(x, source.getInstances(), group))
                                    .compose(x -> Main.productService.getProductWIthInstance(env,product.getSpec(),product.getGroup(),product.getModel()))
                                    .onComplete(promise);
                        } else {
                            Main.productService.getProductWIthInstance(env, product.getSpec(), product.getGroup(), product.getModel())
                                    .compose(x -> saveInstances(x, product.getSource().getInstances(), group))
                                    .compose(x -> Main.productService.getProductWIthInstance(env,product.getSpec(),product.getGroup(),product.getModel()))
                                    .onComplete(promise);
                        }
                    } else {
                        promise.fail("模板不存在，请先同步模板");
                    }
                });

        return promise.future();
    }

    private Future<Void> saveInstances(Product product, Map<Integer, Instance> instances, Group group) {
        Promise<Void> promise = Promise.promise();

        CompositeFuture.all(instances.values().stream()
                .map(x -> Main.productService.saveInstance(product.getId(), x.getVersion(), x.getContent(), group))
                .collect(Collectors.toList()))
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        promise.complete();
                    } else {
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }
}
