package cn.geekcity.xiot.service;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.service.impl.ProductServiceImpl;
import cn.geekcity.xiot.service.impl.TemplateServiceImpl;
import cn.geekcity.xiot.spec.definition.urn.Urn;
import cn.geekcity.xiot.spec.instance.Device;
import cn.geekcity.xiot.spec.template.DeviceTemplate;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.List;

public interface TemplateService {

    static TemplateService create(Vertx vertx) {
        return new TemplateServiceImpl(vertx);
    }

    Future<List<DeviceTemplate>> getTemplates(EnvEnum env);

    Future<DeviceTemplate> save(String currentGroup, DeviceTemplate template);

    Future<DeviceTemplate> getTemplate(EnvEnum env, Urn type);
}
