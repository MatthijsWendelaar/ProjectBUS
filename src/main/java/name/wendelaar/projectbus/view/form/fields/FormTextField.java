package name.wendelaar.projectbus.view.form.fields;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class FormTextField implements FormField {

    private TextField textField = new TextField();

    @Override
    public Object getInput() {
        return textField.getText();
    }

    @Override
    public void clear() {
        textField.clear();
    }

    @Override
    public Control getControl() {
        return textField;
    }
}
