package name.wendelaar.projectbus.view.util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PaneHelper {

    private Pane pane;

    public PaneHelper(Pane pane) {
        this.pane = pane;
    }

    public void clearPane() {
        pane.getChildren().clear();
    }

    public void addItem(Node node) {
        pane.getChildren().add(node);
    }

    public void clearAndAdd(Node node) {
        clearPane();
        addItem(node);
    }
}
