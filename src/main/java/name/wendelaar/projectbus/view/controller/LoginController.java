package name.wendelaar.projectbus.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import name.wendelaar.projectbus.database.concurrency.SimpleReceiveTask;
import name.wendelaar.projectbus.database.manager.IAuthenticationManager;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.validator.InputValidator;
import name.wendelaar.projectbus.validator.ValidatorException;
import name.wendelaar.projectbus.view.ViewState;

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

    @FXML
    private void tryLogin() {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        try {
            InputValidator.inputFilled("All fields need to be filled", username, password);

            IAuthenticationManager manager = LlsApi.getAuthManager();
            SimpleReceiveTask<Boolean> authenticateTask = new SimpleReceiveTask<Boolean>() {
                @Override
                public Boolean execute() {
                    return manager.authenticate(username, password);
                }
            };

            authenticateTask.setOnSucceeded(wk -> {
                User user;
                try {
                    InputValidator.isTrue("The credentials did not match", authenticateTask.getValue());

                    user = manager.getCurrentUser();

                    InputValidator.isTrue("Your account is disabled, please contact a librarian if you think this is a mistake", !user.isAccountDisabled());
                } catch (ValidatorException ex) {
                    return;
                }

                if (user.isLibrarian()) {
                    viewManager.changeState(ViewState.INDEX_LIBRARIAN);
                } else {
                    viewManager.changeState(ViewState.INDEX_DEFAULT);
                }
            });

            LlsApi.getController().getExecutorService().submit(authenticateTask);
        } catch (ValidatorException ex) {
        }
    }
}
