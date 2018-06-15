package name.wendelaar.projectbus.view.form.fields;

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class FormDateField implements FormField {

    private DatePicker datePicker = new DatePicker();

    @Override
    public Object getInput() {
        return datePicker.getValue();
    }

    @Override
    public void clear() {
        datePicker.setValue(LocalDate.now());
    }

    @Override
    public Control getControl() {
        return datePicker;
    }
}
