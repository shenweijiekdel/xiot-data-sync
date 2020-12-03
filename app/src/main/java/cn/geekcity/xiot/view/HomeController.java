package cn.geekcity.xiot.view;

import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.StageType;
import cn.geekcity.xiot.domain.Instance;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.service.ProductDiffer;
import cn.geekcity.xiot.spec.codec.vertx.instance.DeviceCodec;
import cn.geekcity.xiot.utils.StageUtils;
import io.vertx.core.Future;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    public TableColumn<Object, Object> product_colGroup;
    public TableColumn<Object, Object> product_operation_col;

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
        return productService.productsWithInstances(envPrefix)
                .map(FXCollections::observableArrayList);
    }

    public void loadAndCompareTargetEnvData() {
        getTargetEnvData()
                .map(x -> markdiff(product_list.getItems(), x))
                .compose(this::renderData);
    }

    private ObservableList<Product> markdiff(List<Product> source, List<Product> target) {
        List<Product> diff = ProductDiffer.diff(source, target);
        return FXCollections.observableArrayList(diff);
    }


    public Future<ObservableList<Product>> getTargetEnvData() {
        String envPrefix = Main.ENV_PREFIX.get(targetEnvBox.getValue());
        return productService.productsWithInstances(envPrefix)
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
        product_colGroup.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("group"));
        product_colDiff.cellValueFactoryProperty().setValue(new PropertyValueFactory<>("diff"));
        product_operation_col.setCellFactory((col) -> new TableCell<Object, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    //如果此列为空默认不添加元素
                    setText(null);
                    setGraphic(null);
                } else {
                    Button btn = new Button("同步");


                    int index = getIndex();
                    Product product = product_list.getItems().get(index);

                    btn.setOnMouseClicked((col1) -> {
                        System.out.println(product.getName());
                    });

                    this.setGraphic(btn);
                }

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
}
