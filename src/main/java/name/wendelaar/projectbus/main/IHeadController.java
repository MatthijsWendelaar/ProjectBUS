package name.wendelaar.projectbus.main;

import name.wendelaar.projectbus.database.manager.IAuthenticationManager;
import name.wendelaar.projectbus.database.manager.IItemManager;
import name.wendelaar.projectbus.database.manager.IReservationManager;
import name.wendelaar.projectbus.database.manager.IUserManager;
import name.wendelaar.projectbus.view.IViewManager;
import name.wendelaar.projectbus.view.ViewState;

import java.util.concurrent.ExecutorService;

public interface IHeadController {

    public IUserManager getUserManager();

    public IAuthenticationManager getAuthManager();

    public IReservationManager getReservationManager();

    public IItemManager getItemManager();

    public IViewManager getViewManager();

    public ViewState getCurrentState();

    public ExecutorService getExecutorService();
}
