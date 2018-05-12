package name.wendelaar.projectbus.view;

import name.wendelaar.matthijs.snowdb.SnowDB;
import name.wendelaar.matthijs.snowdb.exceptions.SnowDBException;
import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.projectbus.database.manager.HeadUserManager;
import name.wendelaar.projectbus.database.manager.ItemManager;
import name.wendelaar.projectbus.database.manager.ReservationManager;
import name.wendelaar.projectbus.manager.*;

public class MainManager implements IHeadController {

    //Manager related fields
    private HeadUserManager userManager;
    private IReservationManager reservationManager;
    private IItemManager itemManager;

    //View related fields
    private ViewState state;
    private ViewManager viewManager;

    public MainManager(ViewState state) {
        try {
            SnowDB.initialize();
        } catch (SnowDBException ex) {
            ex.printStackTrace();
        }

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


}
