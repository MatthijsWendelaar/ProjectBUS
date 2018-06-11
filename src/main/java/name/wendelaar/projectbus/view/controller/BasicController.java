package name.wendelaar.projectbus.view.controller;

import javafx.scene.Node;

public abstract class BasicController extends Controller {

    private Node selectedTab = null;

    public boolean isSelected(Node node) {
        if (node == null) {
            return false;
        }
        if (node.equals(selectedTab)) {
            return true;
        }
        selectedTab = node;
        return false;
    }
}
