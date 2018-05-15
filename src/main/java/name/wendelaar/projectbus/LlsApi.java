package name.wendelaar.projectbus;

import name.wendelaar.projectbus.manager.*;

public class LlsApi {

    private static IHeadController controller;

    private LlsApi(){}

    public static void receiveController(IHeadController headController) {
        if (LlsApi.controller != null) {
            throw new IllegalStateException("The HeadController can only be set once!");
        }
        LlsApi.controller = headController;
    }

    public static IHeadController getController() {
        return controller;
    }

    public static IViewManager getViewManager() {
        return controller.getViewManager();
    }

    public static IUserManager getUserManager() {
        return controller.getUserManager();
    }

    public static IAuthenticationManager getAuthManager() {
        return controller.getAuthManager();
    }

    public static IItemManager getItemManager() {
        return controller.getItemManager();
    }

    public static IReservationManager getReservationManager() {
        return controller.getReservationManager();
    }
}
