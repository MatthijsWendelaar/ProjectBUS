package name.wendelaar.projectbus.view.form.validator;

import name.wendelaar.projectbus.view.form.FormValidatorException;
import name.wendelaar.projectbus.view.form.fields.FormField;

import java.util.Map;

public class AllFilledValidator implements IFormValidator {

    @Override
    public void validate(Map<String, FormField> inputMap) throws FormValidatorException {
        for (FormField formField : inputMap.values()) {
            String data = formField.getInput().toString();

            if (data != null && !data.isEmpty()) {
                continue;
            }

            throw new FormValidatorException("All fields need to be filled");
        }
    }
}
