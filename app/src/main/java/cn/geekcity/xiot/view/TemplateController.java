package cn.geekcity.xiot.view;

import cn.geekcity.xiot.*;
import cn.geekcity.xiot.domain.DataDiffer;
import cn.geekcity.xiot.domain.Group;
import cn.geekcity.xiot.domain.Product;
import cn.geekcity.xiot.domain.VTemplate;
import cn.geekcity.xiot.service.ProductDiffer;
import cn.geekcity.xiot.service.TemplateDiffer;
import cn.geekcity.xiot.service.TemplateSyncService;
import cn.geekcity.xiot.spec.template.DeviceTemplate;
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
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.geekcity.xiot.Main.productService;
import static cn.geekcity.xiot.Main.templateService;

public class TemplateController extends AbstractController {

    private final TemplateSyncService syncService;
    private final DataDiffer<DeviceTemplate> differ = new TemplateDiffer();
    public TableView<VTemplate> templateView;
    public Label currentEnvironmentLabel;
    public ChoiceBox<Group> currentGroup;
    public ChoiceBox<EnvEnum> dataEnvChoiceBox;
    public TableColumn<Object, Object> operationCol;

    public TemplateController() {
        super(StageType.TEMPLATE);
        syncService = TemplateSyncService.create();
    }

    @Override
    protected void onShown(WindowEvent event) {
        initDataEnvChoiceBox();
        initGroupChoiceBox();
        initOperationCol();
        currentGroup.setConverter(new GroupConverter(currentGroup));
        loadGroups();
        loadDataAndDiff(dataEnvChoiceBox.getValue());
    }

    private void loadCurrentData() {
        ObservableList<VTemplate> items = templateView.getItems();
        for (int i = 0; i < items.size(); i++) {
            int finalI = i;
            templateService.getTemplate(LocalStorage.getEnv(), items.get(i).getSource().type())
                    .onComplete(ar -> {
                        if (ar.succeeded()) {
                            items.get(finalI).setCurrent(ar.result());
                        } else {
                            logger.error("template {} get currentEnv error", items.get(finalI).getSource().type().toString());
                            items.get(finalI).setCurrent(null);
                        }
                    });
        }
    }

    private void initGroupChoiceBox() {
        currentGroup.setConverter(new GroupConverter(currentGroup));
    }

    private Future<Void> loadGroups() {
        Promise<Void> promise = Promise.promise();

        EnvEnum env = LocalStorage.getEnv();
        Main.groupService.available(env)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        Platform.runLater(() -> {
                            currentGroup.setItems(FXCollections.observableArrayList(ar.result()));
                            promise.complete();
                        });
                    } else {
                        StageManager.alert(Alert.AlertType.ERROR, "加载数据失败", ar.cause().getMessage());
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
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

        templateService.getTemplates(env)
                .map(x -> x.stream().map(x1 -> new VTemplate(x1, differ)).collect(Collectors.toList()))
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        Platform.runLater(() -> {
                            templateView.setItems(FXCollections.observableArrayList(ar.result()));
                            promise.complete();
                        });

                    } else {
                        StageManager.alert(Alert.AlertType.ERROR, "加载数据失败", ar.cause().getMessage());
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }

    private void initDataEnvChoiceBox() {
        dataEnvChoiceBox.setConverter(new EnvConverter());
        List<EnvEnum> fromDevs = Stream.of(EnvEnum.Dev, EnvEnum.Stage, EnvEnum.Preview, EnvEnum.Prod).filter(x -> x != LocalStorage.getEnv()).collect(Collectors.toList());
        dataEnvChoiceBox.setItems(FXCollections.observableArrayList(fromDevs));
        dataEnvChoiceBox.setValue(fromDevs.get(0));
        dataEnvChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::handleDataEnvChanged);

    }

    private void handleDataEnvChanged(Observable observable, EnvEnum oldValue, EnvEnum newValue) {
        loadDataAndDiff(newValue);
    }

    private void initOperationCol() {
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
                    VTemplate template = templateView.getItems().get(index);

                    btn.setOnMouseClicked((col1) -> {
                        Group currentGroup = TemplateController.this.currentGroup.getValue();
                        if (currentGroup == null) {
                            StageManager.alert(Alert.AlertType.ERROR, "错误", "请选择开发组");
                            return;
                        }

                        if (!template.getSource().type().groupId().equals(currentGroup.getCode())) {
                            StageManager.alert(Alert.AlertType.ERROR, "错误", "请选择和源数据相同的开发组");
                            return;
                        }

                        syncService.sync(String.valueOf(currentGroup.getId()), template)
                                .onComplete(ar -> {
                                    if (ar.succeeded()) {
                                        template.setCurrent(ar.result());
                                    } else {
                                        StageManager.alert(Alert.AlertType.ERROR, "同步失败", ar.cause().getMessage());
                                    }
                                });
                    });

                    this.setGraphic(btn);
                }
            }
        });
    }

}
