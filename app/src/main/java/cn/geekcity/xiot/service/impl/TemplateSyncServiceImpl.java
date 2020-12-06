package cn.geekcity.xiot.service.impl;

import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.domain.VTemplate;
import cn.geekcity.xiot.service.TemplateSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TemplateSyncServiceImpl implements TemplateSyncService {

    private final Logger logger = LoggerFactory.getLogger(TemplateSyncServiceImpl.class);

    @Override
    public void sync(String currentGroup, VTemplate template) {
        logger.info("sync template ...");

        if (template.getDiff().isEmpty()) {
            logger.info("no diff skip");
            return;
        }

        Main.templateService.save(currentGroup, template.getSource())
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        template.setSource(ar.result());
                    } else {
                        logger.error("sync failed: {}", ar.cause().getMessage());
                    }
                });
    }
}
