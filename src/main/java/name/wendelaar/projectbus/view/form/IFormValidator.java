package name.wendelaar.projectbus.view.form;

import javafx.scene.control.TextInputControl;

import java.util.Map;

public interface IFormValidator {

    public void validate(Map<String, TextInputControl> inputMap) throws FormValidatorException;
}
