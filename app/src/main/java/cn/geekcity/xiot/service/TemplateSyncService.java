package cn.geekcity.xiot.service;

import cn.geekcity.xiot.domain.VTemplate;
import cn.geekcity.xiot.service.impl.TemplateSyncServiceImpl;
import cn.geekcity.xiot.spec.template.DeviceTemplate;
import io.vertx.core.Future;

public interface TemplateSyncService {

    static TemplateSyncService create() {
        return new TemplateSyncServiceImpl();
    }

    void sync(String currentGroup, VTemplate template);
}
