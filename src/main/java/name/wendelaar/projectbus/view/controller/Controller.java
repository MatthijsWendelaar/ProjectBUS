package name.wendelaar.projectbus.view.controller;

import name.wendelaar.projectbus.manager.IViewManager;

public abstract class Controller {

    protected IViewManager viewManager;

    public abstract String getTitle();

    public void receiveViewManager(IViewManager viewManager) {
        if (this.viewManager != null) {
            throw new IllegalStateException("The controller already has a view manager assigned!");
        }
        this.viewManager = viewManager;
    }
}
