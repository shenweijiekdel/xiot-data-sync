package cn.geekcity.xiot.service;

import cn.geekcity.xiot.service.impl.AccountServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public interface AccountService {

    static AccountService create(Vertx vertx) {
        return new AccountServiceImpl(vertx);
    }

    Future<JsonObject> login(String username, String password);
}
