package name.wendelaar.projectbus;

import name.wendelaar.projectbus.manager.*;

public class ProjectBusAPI {

    private static IHeadController controller;

    private ProjectBusAPI(){}

    public static void receiveController(IHeadController headController) {
        if (ProjectBusAPI.controller != null) {
            throw new IllegalStateException("The HeadController can only be set once!");
        }
        ProjectBusAPI.controller = headController;
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

    public static IAuthenticationManager getAuthenticationManager() {
        return controller.getAuthManager();
    }

    public static IItemManager getItemManager() {
        return controller.getItemManager();
    }

    public static IReservationManager getReservationManager() {
        return controller.getReservationManager();
    }
}
