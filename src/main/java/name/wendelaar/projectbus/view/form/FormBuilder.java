package name.wendelaar.projectbus.view.form;

import javafx.fxml.FXMLLoader;

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

    public FormBuilder addField(String name, String key, boolean secret) {
        if (secret) {
            form.addSecretField(name, key);
        } else {
            form.addInputField(name, key);
        }
        return this;
    }

    public FormBuilder addDefaultField(String name, String key) {
        form.addInputField(name, key);
        return this;
    }

    public FormBuilder setReceiver(IFormReceiver receiver) {
        form.setReceiver(receiver);
        return this;
    }

    public FormBuilder setValidator(IFormValidator validator) {
        form.setValidator(validator);
        return this;
    }

    public Form build() {
        return form;
    }
}
