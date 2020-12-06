package cn.geekcity.xiot;

import cn.geekcity.xiot.service.AccountService;
import cn.geekcity.xiot.service.GroupService;
import cn.geekcity.xiot.service.ProductService;
import cn.geekcity.xiot.service.TemplateService;
import cn.geekcity.xiot.utils.StageManager;
import io.vertx.core.Vertx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Vertx vertx = Vertx.vertx();
    public static final AccountService account = AccountService.create(vertx);
    public static final ProductService productService = ProductService.create(vertx);
    public static final GroupService groupService = GroupService.create(vertx);
    public static final TemplateService templateService = TemplateService.create(vertx);

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.hide();
        initLoginStage();
        initProductStage();
        initSelectStage();
        initTemplateStage();
        loadStage();
    }

    private void loadStage() {
        Stage stage = StageManager.getStage(StageType.HOME);
        stage.show();
    }

    private void initLoginStage() throws IOException {
        Stage stage = StageManager.getStage(StageType.LOGIN);
        Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
        stage.setTitle("登录");
        stage.setScene(new Scene(root, 500, 300));
    }

    private void initSelectStage() throws IOException {
        Stage stage = StageManager.getStage(StageType.HOME);
        Parent root = FXMLLoader.load(getClass().getResource("/select_view.fxml"));
        stage.setTitle("IoT控制台数据同步工具");
        stage.setScene(new Scene(root, 500, 300));

    }

    private void initProductStage() throws IOException {
        Stage stage = StageManager.getStage(StageType.PRODUCT);
        Parent root = FXMLLoader.load(getClass().getResource("/product_view.fxml"));
        stage.setTitle("产品同步");
        stage.setScene(new Scene(root, 1024, 768));
    }

    private void initTemplateStage() throws IOException {
        Stage stage = StageManager.getStage(StageType.TEMPLATE);
        Parent root = FXMLLoader.load(getClass().getResource("/template_view.fxml"));
        stage.setTitle("模板同步");
        stage.setScene(new Scene(root, 1024, 768));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
