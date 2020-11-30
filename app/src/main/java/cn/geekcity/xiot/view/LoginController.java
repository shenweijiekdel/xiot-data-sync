package cn.geekcity.xiot.view;

import cn.geekcity.xiot.LocalStorage;
import cn.geekcity.xiot.Main;
import cn.geekcity.xiot.StageType;
import cn.geekcity.xiot.utils.StageUtils;
import cn.geekcity.xiot.utils.md5.Md5Utils;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    private final Stage stage = StageUtils.getStage(StageType.LOGIN_STAGE);
    public TextField tx_username;
    public PasswordField tx_password;
    public Button button;

    public void handleLogin(ActionEvent event) {
        String username = tx_username.textProperty().get();
        String password = tx_password.textProperty().get();
        if (username.isEmpty() || password.isEmpty()) {
            alert(Alert.AlertType.ERROR, "必须输入用户名或密码");
            return ;
        }

        Main.account.login(username, Md5Utils.md5(password))
                .compose(this::putUserInfo)
                .compose(x -> toHomePage())
                .onFailure(this::handleFail);
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

    private Future<Void> toHomePage() {
        Platform.runLater(() -> {
            stage.hide();
            StageUtils.getStage(StageType.HOME_STAGE).show();
        });

        return Future.succeededFuture();
    }

    private Future<Void> putUserInfo(JsonObject userInfo) {
        String token = userInfo.getString("token");
        LocalStorage.put("token", token);
        LocalStorage.put("user", userInfo.encode());
        return Future.succeededFuture();
    }
}
