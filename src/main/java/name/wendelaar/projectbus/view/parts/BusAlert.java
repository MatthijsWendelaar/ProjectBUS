package name.wendelaar.projectbus.view.parts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class BusAlert extends Alert {

    public BusAlert() {
        this(AlertType.NONE);
        getDialogPane().getButtonTypes().add(ButtonType.OK);
    }

    public BusAlert(AlertType type) {
        super(type);
        //((Stage) getDialogPane().getScene().getWindow()).initStyle(StageStyle.DECORATED); //TODO: Think about if a alert needs a border or not.
    }

    public BusAlert addDefaultStyleSheet() {
        return addStyleSheet("busAlert.css");
    }

    public BusAlert addStyleSheet(String stylesheet) {
        URL url = getClass().getResource("/css/" + stylesheet);
        if (url == null) {
            return this;
        }
        getDialogPane().getStylesheets().add(url.toExternalForm());
        return this;
    }

    public BusAlert setMessage(String message) {
        setContentText(message);
        return this;
    }

    public BusAlert addButton(ButtonType type) {
        getDialogPane().getButtonTypes().add(type);
        return this;
    }
}
