package cn.geekcity.xiot.view;

import cn.geekcity.xiot.LocalStorage;
import cn.geekcity.xiot.StageType;
import cn.geekcity.xiot.utils.StageManager;
import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;

public class SelectController extends AbstractController {


    public SelectController() {
        super(StageManager.getStage(StageType.HOME));
    }

    @Override
    protected void onShown(WindowEvent event) {

    }

    @Override
    protected void onShowing(WindowEvent event) {
        String token = LocalStorage.getToken();
        if (token == null) {
            startStage(StageManager.getStage(StageType.LOGIN), true);
        }
    }

    public void handleProductClicked(ActionEvent event) {
        startStage(StageManager.getStage(StageType.PRODUCT), false);
    }

    public void handleTemplateClicked(ActionEvent event) {
        startStage(StageManager.getStage(StageType.TEMPLATE), false);
    }

    public void handleSpecUnitClicked(ActionEvent event) {

    }

    public void handleSpecFormatClicked(ActionEvent event) {

    }

    public void handleSpecPropertyClicked(ActionEvent event) {

    }

    public void handleSpecEventClicked(ActionEvent event) {

    }

    public void handleSpecActionClicked(ActionEvent event) {

    }

    public void handleSpecServiceClicked(ActionEvent event) {

    }

    public void handleSpecDeviceClicked(ActionEvent event) {

    }
}
