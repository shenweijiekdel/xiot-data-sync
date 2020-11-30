package cn.geekcity.xiot.view;

import cn.geekcity.xiot.StageType;
import cn.geekcity.xiot.utils.StageUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeController {
    private final Stage stage = StageUtils.getStage(StageType.LOGIN_STAGE);
    public Button button;
    public ListView lv;

    public TableView table;
}
