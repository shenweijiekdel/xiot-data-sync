package cn.geekcity.xiot.view;

import cn.geekcity.xiot.*;
import cn.geekcity.xiot.domain.*;
import cn.geekcity.xiot.service.ProductDiffer;
import cn.geekcity.xiot.service.ProductSyncService;
import cn.geekcity.xiot.spec.codec.vertx.instance.DeviceCodec;
import cn.geekcity.xiot.utils.StageManager;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.geekcity.xiot.Main.productService;
import static cn.geekcity.xiot.Main.templateService;

public class ProductController extends AbstractController {

    private final DataDiffer<Product> differ = new ProductDiffer();
    private final ProductSyncService syncService = ProductSyncService.create();
    public TableView<VProduct> product_list;
    public ChoiceBox<EnvEnum> fromEnvBox;
    public ChoiceBox<Group> currentGroup;
    public Label currentEnvironmentLabel;
    public Button diffButton;
    public TableColumn<Object, Object> operationCol;
    private ProgressFrom loading;

    public ProductController() {
        super(StageManager.getStage(StageType.PRODUCT));
    }

    @Override
    protected void onShown(WindowEvent event) {
        loading = new ProgressFrom("正在加载数据...", stage);
        initFromEnvBox();
        currentEnvironmentLabel.textProperty().setValue(Objects.requireNonNull(LocalStorage.getEnv()).name());
        initProductColumns();
        initCurrentGroupBox();
        loadDataAndDiff(fromEnvBox.getValue());
    }

    private void initFromEnvBox() {
        fromEnvBox.setConverter(new EnvConverter());
        List<EnvEnum> fromDevs = Stream.of(EnvEnum.Dev, EnvEnum.Stage, EnvEnum.Preview, EnvEnum.Prod).filter(x -> x != LocalStorage.getEnv()).collect(Collectors.toList());
        fromEnvBox.getItems().addAll(fromDevs);
        fromEnvBox.setValue(fromDevs.get(0));
        fromEnvBox.getSelectionModel().selectedItemProperty().addListener(this::handleEnvChanged);

    }

    private void initCurrentGroupBox() {
        currentGroup.setConverter(new GroupConverter(currentGroup));
        EnvEnum value = fromEnvBox.getValue();
        Main.groupService.available(value)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        currentGroup.setItems(FXCollections.observableArrayList(ar.result()));
                    } else {
                        handleFail(ar.cause());
                    }
                });
    }


    private void handleEnvChanged(Observable observable, EnvEnum oldValue, EnvEnum newValue) {
        loadDataAndDiff(newValue);
    }


    private void initProductColumns() {
        operationCol.setCellFactory((col) -> new TableCell<Object, Object>() {
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
                    VProduct product = product_list.getItems().get(index);

                    btn.setOnMouseClicked((col1) -> {
                        Group currentGroup = ProductController.this.currentGroup.getValue();
                        if (currentGroup == null) {
                            StageManager.alert(Alert.AlertType.ERROR, "错误", "请选择开发组");
                            return;
                        }

                        if (!product.getSource().getGroup().equals(currentGroup.getCode())) {
                            StageManager.alert(Alert.AlertType.ERROR, "错误", "请选择和源数据相同的开发组");
                            return;
                        }

                        syncService.sync(String.valueOf(currentGroup.getId()), product);
                    });

                    this.setGraphic(btn);
                }
            }
        });
    }

    private void handleFail(Throwable throwable) {
        StageManager.alert(Alert.AlertType.ERROR, "错误", throwable.getMessage());
    }

    private void loadCurrentData() {
        ObservableList<VProduct> items = product_list.getItems();
        for (int i = 0; i < items.size(); i++) {
            int finalI = i;
            Product p = items.get(i).getSource();
            productService.getProductWIthInstance(LocalStorage.getEnv(), p.getSpec(), p.getGroup(), p.getModel())
                    .onComplete(ar -> {
                        if (ar.succeeded()) {
                            items.get(finalI).setCurrent(ar.result());
                        } else {
                            logger.error("product {} get currentEnv error", p.getId());
                            items.get(finalI).setCurrent(null);
                        }
                    });
        }
    }

    private void loadDataAndDiff(EnvEnum env) {
        getData(env)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        loadCurrentData();
                    }
                });
    }

    private Future<Void> getData(EnvEnum env) {
        Promise<Void> promise = Promise.promise();

        productService.productsWithInstances(env)
                .map(x -> x.stream().map(x1 -> new VProduct(x1, differ)).collect(Collectors.toList()))
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        Platform.runLater(() -> {
                            product_list.setItems(FXCollections.observableArrayList(ar.result()));
                            promise.complete();
                        });

                    } else {
                        StageManager.alert(Alert.AlertType.ERROR, "加载数据失败", ar.cause().getMessage());
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }
}
