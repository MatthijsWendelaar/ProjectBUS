package name.wendelaar.projectbus.view.controller;

import javafx.stage.Stage;
import name.wendelaar.projectbus.view.IViewManager;

public abstract class Controller {

    protected int MIN_WIDTH_SIZE = 560;
    protected int MIN_HEIGHT_SIZE = 350;
    protected int MAX_WIDTH_SIZE = 650;
    protected int MAX_HEIGHT_SIZE = 450;

    protected IViewManager viewManager;

    public abstract String getTitle();

    public void receiveViewManager(IViewManager viewManager) {
        if (this.viewManager != null) {
            throw new IllegalStateException("The controller already has a view manager assigned!");
        }
        this.viewManager = viewManager;
        Stage stage = viewManager.getStage();
        stage.setMinWidth(MIN_WIDTH_SIZE);
        stage.setMinHeight(MIN_HEIGHT_SIZE);
        stage.setMaxWidth(MAX_WIDTH_SIZE);
        stage.setMaxHeight(MAX_HEIGHT_SIZE);
    }

    public void setupAfterInitialization() {

    }
}
