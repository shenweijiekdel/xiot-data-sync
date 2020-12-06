package cn.geekcity.xiot.service;

import cn.geekcity.xiot.EnvEnum;
import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.service.impl.GroupServiceImpl;
import cn.geekcity.xiot.spec.codec.vertx.instance.ActionCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.EventCodec;
import cn.geekcity.xiot.spec.codec.vertx.instance.PropertyCodec;
import cn.geekcity.xiot.spec.instance.*;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GroupService {

    static GroupService create(Vertx vertx) {
        return new GroupServiceImpl(vertx);
    }

    Future<List<Group>> available(EnvEnum env);
}
