package name.wendelaar.projectbus.view.form.fields;

import javafx.scene.control.Control;

public interface FormField {

    public void clear();

    public Object getInput();

    public Control getControl();
}
