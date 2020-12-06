package cn.geekcity.xiot.view;

import cn.geekcity.xiot.StageType;
import cn.geekcity.xiot.utils.StageManager;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(AbstractController.class);
    protected Stage stage;

    public AbstractController(Stage stage) {
        this.stage = stage;
        this.stage.onShowingProperty().setValue(this::onShowing);
        this.stage.onShownProperty().setValue(this::onShown);
        this.stage.onHiddenProperty().setValue(this::onHidden);
        this.stage.onHidingProperty().setValue(this::onHiding);
        this.stage.onCloseRequestProperty().setValue(this::onClose);
    }

    public AbstractController(StageType type) {
        this(StageManager.getStage(type));
    }

    protected void onClose(WindowEvent event) {
        logger.info("stage {}: onClose", stage.getTitle());
    }

    protected void onShowing(WindowEvent event) {
        logger.info("stage {}: onShowing", stage.getTitle());
    }

    protected void onHiding(WindowEvent event) {
        logger.info("stage {}: onHiding", stage.getTitle());
    }

    protected void onHidden(WindowEvent event) {
        logger.info("stage {}: onHidden", stage.getTitle());
    }

    protected void onShown(WindowEvent event) {
        logger.info("stage {}: onShown", stage.getTitle());
    }

    protected final void startStage(Stage targetStage, boolean closeMyself) {
        Platform.runLater(() -> {
            targetStage.show();
            if (closeMyself) {
                this.stage.close();
            }
        });
    }
}
