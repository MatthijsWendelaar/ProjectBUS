package name.wendelaar.projectbus.view;

import javafx.application.Application;
import javafx.stage.Stage;
import name.wendelaar.projectbus.ProjectBusAPI;
import name.wendelaar.projectbus.database.manager.HeadUserManager;
import name.wendelaar.projectbus.database.manager.ItemManager;
import name.wendelaar.projectbus.database.manager.ReservationManager;
import name.wendelaar.projectbus.manager.*;

public class MainManager extends Application implements IViewManager, IHeadController {

    //Manager related fields
    private HeadUserManager userManager;
    private IReservationManager reservationManager;
    private IItemManager itemManager;

    //View related fields
    private ViewState state;

    private boolean initialized = false;

    public MainManager(ViewState state) {
        this.state = state;
        this.userManager = new HeadUserManager(this);
        this.reservationManager = new ReservationManager(this);
        this.itemManager = new ItemManager(this);
        ProjectBusAPI.receiveController(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("The manager has already been initialized");
        }
        launch();
        initialized = true;
    }

    @Override
    public void changeState(ViewState state) {
        if (state == null || state == this.state) {
            return;
        }
        this.state = state;
        //TODO: Change the view!
    }

    @Override
    public ViewState getCurrentState() {
        return state;
    }

    @Override
    public IUserManager getUserManager() {
        return userManager;
    }

    @Override
    public IAuthenticationManager getAuthManager() {
        return userManager;
    }

    @Override
    public IReservationManager getReservationManager() {
        return reservationManager;
    }

    @Override
    public IItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public IViewManager getViewManager() {
        return this;
    }
}
