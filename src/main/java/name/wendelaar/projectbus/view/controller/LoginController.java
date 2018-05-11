package name.wendelaar.projectbus.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import name.wendelaar.projectbus.ProjectBusAPI;
import name.wendelaar.projectbus.database.models.User;

public class LoginController extends Controller {

    private String title = "Login";

    @FXML
    private Button actionButton;

    @Override
    public String getTitle() {
        return this.title;
    }

    public void test(ActionEvent event) {
        User user = ProjectBusAPI.getAuthenticationManager().authenticate("test", "test");
        if (user == null) {
            System.out.println("EMPTY!");
        } else {
            System.out.println("FULL: " + user.getUsername());
        }
    }
}
