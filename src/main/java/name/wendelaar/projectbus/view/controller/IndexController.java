package name.wendelaar.projectbus.view.controller;

import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.projectbus.view.parts.BusAlert;

public class IndexController extends Controller {

    private String title = "Home";

    @Override
    public String getTitle() {
        return title;
    }

    public IndexController() {
        new BusAlert().addDefaultStyleSheet().setMessage("Welcome " + LlsApi.getAuthManager().getCurrentUser().getUsername()).showAndWait();
    }
}
