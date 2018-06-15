package name.wendelaar.projectbus.view.form;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import name.wendelaar.projectbus.view.form.fields.FormField;
import name.wendelaar.projectbus.view.form.validator.IFormValidator;
import name.wendelaar.projectbus.view.parts.BusAlert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Form {

    @FXML
    private Button formSubmitButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox verticalInputBodyBox;

    private Parent root;
    private IFormReceiver receiver;

    private List<IFormValidator> validators = new ArrayList<>();

    private Map<String, FormField> inputMap = new HashMap<>();

    @FXML
    private void onFormSubmit() {
        try {
            for (IFormValidator validator : validators) {
                validator.validate(inputMap);
            }
        } catch (FormValidatorException ex) {
            new BusAlert().addDefaultIcon().addDefaultStyleSheet().setMessage(ex.getMessage()).showAndWait();
            return;
        }

        if (receiver == null) {
            return;
        }

        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, FormField> entry : inputMap.entrySet()) {
            data.put(entry.getKey(), entry.getValue().getInput());
        }

        receiver.receive(data);
    }

    private void initialize() {
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            verticalInputBodyBox.setPrefWidth(newValue.doubleValue() - 20);
        });
        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            verticalInputBodyBox.setPrefHeight(newValue.doubleValue() - 10);
        });
    }

    public void setRoot(Parent root) {
        this.root = root;
        initialize();
    }

    public Parent getRoot() {
        return root;
    }

    public void setReceiver(IFormReceiver receiver) {
        this.receiver = receiver;
    }

    public void addValidator(IFormValidator validator) {
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    public void clearForm() {
        for (FormField formField : inputMap.values()) {
            formField.clear();
        }
    }

    public void addField(String text, String key, FormField formField) {
        if (key == null || key.isEmpty() || inputMap.containsKey(key)) {
            return;
        }

        Text inputDisplayText = new Text(text == null ? key : text + ":");

        inputMap.put(key, formField);

        addMargin(inputDisplayText);
        addMargin(formField.getControl());

        verticalInputBodyBox.getChildren().addAll(inputDisplayText, formField.getControl());
    }

    private void addMargin(Node node) {
        VBox.setMargin(node, new Insets(3, 0, 6, 0));
        VBox.setVgrow(node, Priority.ALWAYS);
    }
}
