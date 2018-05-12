package name.wendelaar.projectbus.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.validator.InputValidator;
import name.wendelaar.projectbus.validator.ValidatorException;
import name.wendelaar.projectbus.view.parts.BusAlert;

public class LoginController extends Controller {

    private String title = "Login";

    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    public LoginController() {
        //MIN_HEIGHT_SIZE = 400;
        //MIN_WIDTH_SIZE = 400;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    public void tryLogin(ActionEvent event) {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        try {
            InputValidator.inputFilled("All fields need to be filled", username, password);

            boolean validAuth = LlsApi.getAuthenticationManager().authenticate(username, password);
            InputValidator.isTrue("The credentials did not match", validAuth);

            new BusAlert().addDefaultStyleSheet().setMessage("Lekker mwoan het is je gelukt! je naam is: " + LlsApi.getAuthenticationManager().getCurrentUser().getUsername()).showAndWait();
        } catch (ValidatorException ex) {}
    }
}
