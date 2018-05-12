package name.wendelaar.projectbus.view.controller;

import javafx.stage.Stage;
import name.wendelaar.projectbus.manager.IViewManager;

public abstract class Controller {

    protected int MIN_WIDTH_SIZE = 560;
    protected int MIN_HEIGHT_SIZE = 350;

    protected IViewManager viewManager;

    public abstract String getTitle();

    public void receiveViewManager(IViewManager viewManager) {
        if (this.viewManager != null) {
            throw new IllegalStateException("The controller already has a view manager assigned!");
        }
        this.viewManager = viewManager;

        Stage stage = viewManager.getStage();
        System.out.println("WIDTH: " + MIN_WIDTH_SIZE);
        System.out.println("HEIGHT: " + MIN_HEIGHT_SIZE);
        stage.setMinWidth(MIN_WIDTH_SIZE);
        stage.setMinHeight(MIN_HEIGHT_SIZE);
        System.out.println("real width first: " + stage.getMinWidth());
        System.out.println("real height first: " + stage.getMinHeight());
    }
}
