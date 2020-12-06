package cn.geekcity.xiot.utils;

import cn.geekcity.xiot.StageType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

    private static final Map<StageType, Stage> stages = new HashMap<>();

    static {
        for (StageType type : StageType.values()) {
            stages.put(type, new Stage());
        }
    }

    private StageManager() {
    }

    public static Stage getStage(StageType type) {
        return stages.get(type);
    }

    public static void alert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.titleProperty().setValue(title);
            alert.headerTextProperty().setValue(message);
            alert.showAndWait();
        });
    }


}
