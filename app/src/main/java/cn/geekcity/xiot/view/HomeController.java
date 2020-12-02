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
import java.util.Optional;
import java.util.stream.Collectors;

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
        initProductColumns();
        envBox.getSelectionModel().selectedItemProperty().addListener(this::handleEnvChanged);
        targetEnvBox.getSelectionModel().selectedItemProperty().addListener(this::handleTargetEnvChanged);
        getSourceData()
                .compose(this::renderData);
    }

    private void handleTargetEnvChanged(Observable observable, String oldValue, String newValue) {
        loadAndCompareTargetEnvData();
    }

    private void handleEnvChanged(Observable observable, String oldValue, String newValue) {
        getSourceData()
                .compose(this::renderData);
    }


    public Future<ObservableList<Product>> getSourceData() {
        String envPrefix = Main.ENV_PREFIX.get(envBox.getValue());
        return productService.products(envPrefix)
                .map(ProductCodec::decode)
                .map(FXCollections::observableArrayList);
    }

    public void loadAndCompareTargetEnvData() {
        getTargetEnvData()
                .map(x -> markdiff(product_list.getItems(), x))
                .compose(this::renderData);
    }

    private ObservableList<Product> markdiff(ObservableList<Product> source, ObservableList<Product> target) {
        return FXCollections.observableArrayList(source.stream().peek(x -> {
            Optional<Product> first = target.stream().filter(x1 -> x1.getModel().equals(x.getModel()) && x1.getSpec().equals(x.getSpec())).findFirst();
            if (!first.isPresent()) {
                x.setDiff("not exists");
                return;
            }

            Product tar = first.get();
            if (!x.getName().equals(tar.getName())) {
                x.setDiff("name");
                return;
            }

            x.setDiff("no diff");
        }).collect(Collectors.toList()));
    }

    public Future<ObservableList<Product>> getTargetEnvData() {
        String envPrefix = Main.ENV_PREFIX.get(targetEnvBox.getValue());
        return productService.products(envPrefix)
                .map(ProductCodec::decode)
                .map(FXCollections::observableArrayList);
    }

    private Future<Void> renderData(ObservableList<Product> data) {
        Platform.runLater(() -> product_list.setItems(data));
        return Future.succeededFuture();
    }

    private void initProductColumns() {
        product_colId.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("id"));
        product_colName.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("name"));
        product_colModel.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("model"));
        product_colNs.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("spec"));
        product_colDiff.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("diff"));
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
}
