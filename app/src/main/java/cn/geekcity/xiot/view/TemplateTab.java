package cn.geekcity.xiot.view;

import cn.geekcity.xiot.domain.VTemplate;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TemplateTab {

    private final Stage primaryStage;

    private final TableView<VTemplate> templateTab = new TableView<>();
    private final TableColumn<Object, Object> typeTableCol = new TableColumn<>();
    private final TableColumn<Object, Object> nameTableCol = new TableColumn<>();

    public TemplateTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        typeTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeTableCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public TableView<VTemplate> getTemplateTab() {
        return templateTab;
    }

    public TableColumn<Object, Object> getTypeTableCol() {
        return typeTableCol;
    }

    public TableColumn<Object, Object> getNameTableCol() {
        return nameTableCol;
    }
}
