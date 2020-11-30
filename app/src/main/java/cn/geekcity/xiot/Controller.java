package cn.geekcity.xiot;

import cn.geekcity.xiot.spec.instance.Device;
import cn.geekcity.xiot.spec.product.Product;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Controller {

    public TextArea textArea;
    public Button button;

    public void handleAction(ActionEvent event) {
        String name = event.getTarget().toString();
        System.out.println(name);
        async()
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        textArea.textProperty().setValue("success");
                    } else {
                        textArea.textProperty().setValue("failed");
                    }
                });
    }

    Future<Void> async() {
        Promise<Void> promise = Promise.promise();

        Main.vertx.executeBlocking(action -> {
            try {
                System.out.println("do timeout");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            action.complete();
        }, promise);

        return promise.future();
    }


}
