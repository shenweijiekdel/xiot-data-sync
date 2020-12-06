package cn.geekcity.xiot.view;

import cn.geekcity.xiot.*;
import cn.geekcity.xiot.utils.StageManager;
import cn.geekcity.xiot.utils.md5.Md5Utils;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public class LoginController extends AbstractController {

    public TextField tx_username;
    public PasswordField tx_password;
    public Button button;
    public ChoiceBox<EnvEnum> envChoiceBox;
    private ProgressFrom progressFrom;

    public LoginController() {
        super(StageManager.getStage(StageType.LOGIN));
    }

    @Override
    protected void onShown(WindowEvent windowEvent) {
        initEnvChoiceBox();
        progressFrom = new ProgressFrom("Loading...",stage);
    }

    public void handleLogin(ActionEvent event) {
        String username = tx_username.textProperty().get();
        String password = tx_password.textProperty().get();
        username = "shenweijiekdel";
        password = "swj7528065";
        if (username.isEmpty() || password.isEmpty()) {
            StageManager.alert(Alert.AlertType.ERROR, "登录失败", "必须输入用户名或密码");
            return;
        }
        Platform.runLater(() -> {
            progressFrom.activateProgressBar();

        });

        Main.account.login(envChoiceBox.getValue(), username, Md5Utils.md5(password))
                .compose(this::putUserInfo)
                .compose(x -> toHomePage())
                .onFailure(this::handleFail);
    }

    private void handleFail(Throwable throwable) {
        Platform.runLater(() -> {
            progressFrom.cancelProgressBar();
        });
        StageManager.alert(Alert.AlertType.ERROR, "登录失败", throwable.getMessage());
    }

    private Future<Void> toHomePage() {
        LocalStorage.setEnv(envChoiceBox.getValue());
        Platform.runLater(() -> {
            progressFrom.cancelProgressBar();
            startStage(StageManager.getStage(StageType.HOME), true);
        });

        return Future.succeededFuture();
    }

    private Future<Void> putUserInfo(JsonObject userInfo) {
        String token = userInfo.getString("token");
        LocalStorage.put("token", token);
        LocalStorage.put("user", userInfo.encode());
        return Future.succeededFuture();
    }

    private void initEnvChoiceBox() {
        envChoiceBox.setConverter(new EnvConverter());
        envChoiceBox.getItems().addAll(EnvEnum.Dev, EnvEnum.Stage, EnvEnum.Preview, EnvEnum.Prod);
        envChoiceBox.setValue(EnvEnum.Dev);
    }
}
