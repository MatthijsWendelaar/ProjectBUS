package name.wendelaar.projectbus.view.form.validator;

import name.wendelaar.projectbus.view.form.FormValidatorException;
import name.wendelaar.projectbus.view.form.fields.FormField;

import java.util.Map;

public interface IFormValidator {

    public void validate(Map<String, FormField> inputMap) throws FormValidatorException;
}
