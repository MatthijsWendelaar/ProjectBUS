package name.wendelaar.projectbus.view.util;

import javafx.scene.image.Image;
import name.wendelaar.projectbus.view.parts.BusAlert;

public class InfoAlertBuilder {

    private StringBuilder builder;

    public InfoAlertBuilder() {
        builder = new StringBuilder();
    }

    public InfoAlertBuilder appendLine(String key, Object value) {
        builder.append(key).append(": ").append(value).append("\n");
        return this;
    }

    public BusAlert buildAlert() {
        return new BusAlert().addDefaultStyleSheet().addIcon(new Image(getClass().getResourceAsStream("/images/LLSIcon.png"))).setMessage(builder.toString());
    }
}
