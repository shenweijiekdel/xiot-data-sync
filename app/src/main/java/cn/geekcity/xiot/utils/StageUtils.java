package cn.geekcity.xiot.utils;

import cn.geekcity.xiot.StageType;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StageUtils {

    private static final Map<StageType, Stage> stages = new HashMap<>();

    private StageUtils() {
    }

    static {
        stages.put(StageType.LOGIN_STAGE,new Stage());
        stages.put(StageType.HOME_STAGE,new Stage());
    }

    public static Stage getStage(StageType type) {
        return stages.get(type);
    }

}
