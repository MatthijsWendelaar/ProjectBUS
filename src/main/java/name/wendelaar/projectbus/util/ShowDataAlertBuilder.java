package name.wendelaar.projectbus.util;

import javafx.scene.image.Image;
import name.wendelaar.projectbus.view.parts.BusAlert;

import java.util.Map;

public class ShowDataAlertBuilder {

    private StringBuilder builder;

    public ShowDataAlertBuilder() {
        builder = new StringBuilder();
    }

    public ShowDataAlertBuilder append(String key, Object value) {
        builder.append(key).append(": ").append(value).append("\n");
        return this;
    }

    public BusAlert buildAlert() {
        return new BusAlert().addDefaultStyleSheet().addIcon(new Image(getClass().getResourceAsStream("/images/LLSIcon.png"))).setMessage(builder.toString());
    }

    public static BusAlert buildAlert(Map<String, Object> values) {
        BusAlert busAlert = new BusAlert().addDefaultStyleSheet();
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            builder.append(entry.getKey()).append(entry.getValue()).append("\n");
        }

        busAlert.setMessage(builder.toString());
        return busAlert;
    }
}
