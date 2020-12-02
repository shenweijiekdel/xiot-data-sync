package cn.geekcity.xiot.view;

import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.StageType;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.ProductCodec;
import cn.geekcity.xiot.domain.ProductDiffer;
import cn.geekcity.xiot.utils.StageUtils;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Arrays;
import java.util.List;

import static cn.geekcity.xiot.Main.productService;

public class HomeController {

    private final Stage stage = StageUtils.getStage(StageType.HOME_STAGE);
    public TableView<Product> product_list;
    public TableColumn<Object, Object> product_colId;
    public TableColumn<Object, Object> product_colName;
    public TableColumn<Object, Object> product_colModel;
    public TableColumn<Object, Object> product_colNs;
    public ChoiceBox<String> envBox;
    public TableColumn<Object, Object> product_colDiff;
    public ChoiceBox<String> targetEnvBox;

    public HomeController() {
        stage.onShownProperty().setValue(this::onShown);
    }

    private void onShown(WindowEvent event) {
        envBox.getSelectionModel().selectedItemProperty().addListener(this::handleEnvChanged);
        envBox.getSelectionModel().selectedItemProperty().addListener(this::handleTargetEnvChanged);
        loadData();
    }

    private void handleTargetEnvChanged(Observable observable, String oldValue, String newValue) {
    }

    private void handleEnvChanged(Observable observable, String oldValue, String newValue) {
        String envPrefix = Main.ENV_PREFIX.get(newValue);
        loadData();
    }


    public Future<ProductDiffer> loadData() {
        Promise<ProductDiffer> promise = Promise.promise();

        String envPrefix = Main.ENV_PREFIX.get(envBox.getValue());
        String targetEnvPrefix = Main.ENV_PREFIX.get(targetEnvBox.getValue());
        productService.products(envPrefix)
                .map(ProductCodec::decode)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        if (envPrefix.equals(targetEnvPrefix)) {
                            promise.complete(new ProductDiffer().source(ar.result()).target(ar.result()));
                        } else {
                            productService.products(targetEnvPrefix)
                                    .map(ProductCodec::decode)
                                    .onComplete(ar2 -> {
                                        if (ar.succeeded()) {
                                            promise.complete(new ProductDiffer().source(ar.result()).target(ar2.result()));
                                        } else {
                                            promise.fail(ar.cause());
                                        }
                                    });
                        }
                    } else {
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }

    public void renderData(List<Product> source, List<Product> target) {

        String sourceEnv = envBox.getValue();
        String targetEnv = envBox.getValue();
        productService.products(sourceEnv)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        Platform.runLater(() -> {
                            List<Product> decode = ProductCodec.decode(ar.result());
                            ObservableList<Product> products = FXCollections.observableArrayList(decode);
                            product_colId.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("id"));
                            product_colName.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("name"));
                            product_colModel.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("model"));
                            product_colNs.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("spec"));
                            product_list.setItems(products);
                        });
                    } else {
                        ar.cause().printStackTrace();
                        handleFail(ar.cause());
                    }
                });
    }

    private void handleFail(Throwable throwable) {
        alert(Alert.AlertType.ERROR, throwable.getMessage());
    }

    private void alert(Alert.AlertType type, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.titleProperty().setValue("登录失败");
            alert.headerTextProperty().setValue(message);
            alert.showAndWait();
        });
    }

    public void handleAction(ActionEvent event) {
        System.out.println(event.getEventType().getName());
        System.out.println(event.getSource().toString());
    }
}
