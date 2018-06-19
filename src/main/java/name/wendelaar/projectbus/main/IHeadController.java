package name.wendelaar.projectbus.main;

import name.wendelaar.projectbus.database.manager.*;
import name.wendelaar.projectbus.view.IViewManager;
import name.wendelaar.projectbus.view.ViewState;

import java.util.concurrent.ExecutorService;

public interface IHeadController {

    public IUserManager getUserManager();

    public IAuthenticationManager getAuthManager();

    public IReservationManager getReservationManager();

    public IItemManager getItemManager();

    public IItemAttributeManager getItemAttributeManager();

    public IViewManager getViewManager();

    public ViewState getCurrentState();

    public ExecutorService getExecutorService();
}
