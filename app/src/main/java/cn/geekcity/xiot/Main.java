package cn.geekcity.xiot;

import cn.geekcity.xiot.service.AccountService;
import cn.geekcity.xiot.service.impl.AccountServiceImpl;
import cn.geekcity.xiot.utils.StageUtils;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.dns.DnsClient;
import io.vertx.core.json.JsonObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class Main extends Application {

    public static Vertx vertx = Vertx.vertx();
    public static AccountService account = AccountService.create(vertx);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.hide();
        initLoginStage();
        initHomeStage();
        loadStage();
    }

    private void loadStage() {
        String token = LocalStorage.get("token");
        if (token == null) {
            StageUtils.getStage(StageType.LOGIN_STAGE).show();
        } else {
            StageUtils.getStage(StageType.HOME_STAGE).show();
        }
    }

    private void initLoginStage() throws IOException {
        Stage stage = StageUtils.getStage(StageType.LOGIN_STAGE);
        Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
        stage.setTitle("登录");
        stage.setScene(new Scene(root, 500, 300));
    }

    private void initHomeStage() throws IOException {
        Stage stage = StageUtils.getStage(StageType.HOME_STAGE);
        Parent root = FXMLLoader.load(getClass().getResource("/home.fxml"));
        stage.setTitle("主页");
        stage.setScene(new Scene(root, 1024, 768));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
