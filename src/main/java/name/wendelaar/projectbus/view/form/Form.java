package name.wendelaar.projectbus.view.form;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import name.wendelaar.projectbus.view.parts.BusAlert;

import java.util.HashMap;
import java.util.Map;

public class Form {

    private Parent root;
    private IFormReceiver receiver;
    private IFormValidator validator;

    @FXML
    private Button formSubmitButton;

    @FXML
    private VBox verticalInputBodyBox;

    private Map<String, TextInputControl> inputMap = new HashMap<>();

    @FXML
    private void onFormSubmit() {
        try {
            if (validator != null) {
                validator.validate(inputMap);
            }
        } catch (FormValidatorException ex) {
            new BusAlert().addDefaultIcon().addDefaultStyleSheet().setMessage(ex.getMessage()).showAndWait();
            return;
        }

        if (receiver == null) {
            return;
        }

        Map<String, String> data = new HashMap<>();
        for (Map.Entry<String, TextInputControl> entry : inputMap.entrySet()) {
            data.put(entry.getKey(), entry.getValue().getText());
        }

        receiver.receive(data);

        debugPrint();
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public Parent getRoot() {
        return root;
    }

    public void setReceiver(IFormReceiver receiver) {
        this.receiver = receiver;
    }

    public IFormReceiver getReceiver() {
        return receiver;
    }

    public void setValidator(IFormValidator validator) {
        this.validator = validator;
    }

    public IFormValidator getValidator() {
        return validator;
    }

    public void addInputField(String text, String inputKey) {
        if (inputKey == null || inputMap.containsKey(inputKey)) {
            return;
        }

        Text inputDisplayText = new Text(text == null ? inputKey : text + ":");

        addField(inputDisplayText, inputKey, new TextField());
    }

    public void addSecretField(String text, String inputKey) {
        if (inputKey == null || inputMap.containsKey(inputKey)) {
            return;
        }
        Text inputDisplayText = new Text(text == null ? inputKey : text + ":");

        addField(inputDisplayText, inputKey, new PasswordField());
    }

    private void addField(Text text, String key, TextInputControl textInput) {
        inputMap.put(key, textInput);
        verticalInputBodyBox.getChildren().addAll(text, textInput);
    }

    public void debugPrint() {
        for (Map.Entry<String, TextInputControl> entry : inputMap.entrySet()) {
            System.out.println("Data Key: " + entry.getKey());
            System.out.println("Value: " + entry.getValue().getText());
        }
    }
}
