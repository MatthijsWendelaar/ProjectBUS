package name.wendelaar.projectbus.view.form.fields;

import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;

public class FormPasswordField implements FormField {

    private PasswordField passwordField = new PasswordField();

    @Override
    public Object getInput() {
        return passwordField.getText();
    }

    @Override
    public Control getControl() {
        return passwordField;
    }

    @Override
    public void clear() {
        passwordField.clear();
    }
}
