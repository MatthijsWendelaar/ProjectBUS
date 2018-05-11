package name.wendelaar.projectbus.manager;

import name.wendelaar.projectbus.view.ViewState;

public interface IHeadController {

    public IUserManager getUserManager();

    public IAuthenticationManager getAuthManager();

    public IReservationManager getReservationManager();

    public IItemManager getItemManager();

    public IViewManager getViewManager();

    public ViewState getCurrentState();
}
