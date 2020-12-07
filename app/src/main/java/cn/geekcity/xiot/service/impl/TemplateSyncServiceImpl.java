package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.domain.VTemplate;
import cn.geekcity.xiot.service.TemplateSyncService;
import cn.geekcity.xiot.spec.template.DeviceTemplate;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TemplateSyncServiceImpl implements TemplateSyncService {

    private final Logger logger = LoggerFactory.getLogger(TemplateSyncServiceImpl.class);

    @Override
    public Future<DeviceTemplate> sync(String currentGroup, VTemplate template) {
        Promise<DeviceTemplate> promise = Promise.promise();

        logger.info("sync template ...");

        if (template.getDiff().isEmpty()) {
            logger.info("no diff skip");
            return promise.future();
        }

        Main.templateService.save(currentGroup, template.getSource())
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        promise.complete(ar.result());
                    } else {
                        logger.error("sync failed: {}", ar.cause().getMessage());
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }
}
