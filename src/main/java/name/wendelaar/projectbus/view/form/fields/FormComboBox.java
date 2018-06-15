package name.wendelaar.projectbus.view.form.fields;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;

import java.util.Map;

public class FormComboBox<T> implements FormField {

    private ComboBox<T> comboBox = new ComboBox<>();
    private Map<T, Object> items;


    public FormComboBox(Map<T, Object> items) {
        comboBox.getItems().addAll(items.keySet());
        this.items = items;
    }

    @Override
    public Object getInput() {
        return items.get(comboBox.getValue());
    }

    @Override
    public void clear() {
        comboBox.getSelectionModel().selectFirst();
    }

    @Override
    public Control getControl() {
        return comboBox;
    }
}
