package name.wendelaar.projectbus.view.util;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class ViewUtil {

    public static void addConstraints(Node node) {
        addConstraints(node, 0.0);
    }

    public static void addConstraints(Node node, double all) {
        addConstraints(node, all, all);
    }

    public static void addConstraints(Node node, double vertical, double horizontal) {
        addConstraints(node, horizontal, horizontal, vertical, vertical);
    }

    public static void addConstraints(Node node, double top, double bottom, double left, double right) {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setBottomAnchor(node, bottom);
    }
}
