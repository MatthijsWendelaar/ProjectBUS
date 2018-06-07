package name.wendelaar.projectbus.main;

import name.wendelaar.projectbus.database.concurrency.factory.DatabaseThreadFactory;
import name.wendelaar.projectbus.view.ViewManager;
import name.wendelaar.projectbus.view.ViewState;
import name.wendelaar.snowdb.SnowDB;
import name.wendelaar.snowdb.exceptions.SnowDBException;
import name.wendelaar.projectbus.database.manager.HeadUserManager;
import name.wendelaar.projectbus.database.manager.ItemManager;
import name.wendelaar.projectbus.database.manager.ReservationManager;
import name.wendelaar.projectbus.manager.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainManager implements IHeadController {

    //Thread related fields
    private ExecutorService executorService;

    //Manager related fields
    private HeadUserManager userManager;
    private IReservationManager reservationManager;
    private IItemManager itemManager;

    //View related fields
    private ViewState state;
    private ViewManager viewManager;

    public MainManager(ViewState state) {
        try {
            SnowDB.getInstance().initialize();
        } catch (SnowDBException ex) {
            ex.printStackTrace();
        }
        executorService = Executors.newFixedThreadPool(5, new DatabaseThreadFactory("DB_Thread_"));

        LlsApi.receiveController(this);
        this.state = state;
        this.userManager = new HeadUserManager(this);
        this.reservationManager = new ReservationManager(this);
        this.itemManager = new ItemManager(this);
        this.viewManager = ViewManager.getInstance();

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
        return viewManager;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }
}
