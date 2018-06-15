package name.wendelaar.projectbus.view.form;

import javafx.fxml.FXMLLoader;
import name.wendelaar.projectbus.view.form.fields.*;
import name.wendelaar.projectbus.view.form.validator.IFormValidator;

import java.io.IOException;

public class FormBuilder {

    private Form form;

    public FormBuilder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/forms/form.fxml"));
            loader.load();
            form = loader.getController();
            form.setRoot(loader.getRoot());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public FormBuilder addTextField(String name, String key) {
        form.addField(name, key, new FormTextField());
        return this;
    }

    public FormBuilder addPasswordField(String name, String key) {
        form.addField(name, key, new FormPasswordField());
        return this;
    }

    public FormBuilder addDateField(String name, String key) {
        form.addField(name, key, new FormDateField());
        return this;
    }

    public FormBuilder addComboBox(String name, String key, FormComboBox comboBox) {
        form.addField(name, key, comboBox);
        return this;
    }

    public FormBuilder addField(String name, String key, FormField formField) {
        form.addField(name, key, formField);
        return this;
    }

    public FormBuilder addValidator(IFormValidator validator) {
        form.addValidator(validator);
        return this;
    }

    public FormBuilder setReceiver(IFormReceiver receiver) {
        form.setReceiver(receiver);
        return this;
    }

    public Form build() {
        return form;
    }
}
