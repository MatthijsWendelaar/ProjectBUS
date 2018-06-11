package name.wendelaar.projectbus.view.form;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import name.wendelaar.simplevalidator.BoolValidator;
import name.wendelaar.simplevalidator.ExcepValidator;

import java.util.HashMap;
import java.util.Map;

public class Form {

    private Parent root;

    @FXML
    private Button formSubmitButton;

    @FXML
    private VBox verticalInputBodyBox;

    private Map<String, TextField> inputMap = new HashMap<>();

    @FXML
    private void onFormSubmit() {
        formSubmitButton.setDisable(true);
        System.out.println("Wat een pret!");
        debugPrint();
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public Parent getRoot() {
        return root;
    }

    public void addInputField(String text, String inputKey) {
        if (inputKey == null || inputMap.containsKey(inputKey)) {
            return;
        }

        Text inputDisplayText = new Text(text == null ? inputKey : text + ":");
        TextField inputField = new TextField();

        inputMap.put(inputKey, inputField);
        verticalInputBodyBox.getChildren().addAll(inputDisplayText, inputField);
    }

    public void debugPrint() {
        for (Map.Entry<String, TextField> entry : inputMap.entrySet()) {
            System.out.println("Data Key: " + entry.getKey());
            System.out.println("Value: " + entry.getValue().getText());
        }
    }
}
