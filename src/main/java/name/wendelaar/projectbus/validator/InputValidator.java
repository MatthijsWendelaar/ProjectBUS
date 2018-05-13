package name.wendelaar.projectbus.validator;

import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.simplevalidator.BoolValidator;

public class InputValidator {

    public static void inputFilled(String message, String... inputs) throws ValidatorException {
        if (!BoolValidator.notEmpty(inputs)) {
            throwValidatorException(message);
        }
    }

    public static void isTrue(String message, boolean... inputs) throws ValidatorException {
        if (!BoolValidator.assertTrue(inputs)) {
            throwValidatorException(message);
        }
    }

    private static void throwValidatorException(String message) throws ValidatorException {
        if (BoolValidator.notNull(message)) {
            showAlert(message);
        }
        throw new ValidatorException();
    }

    private static void showAlert(String message) {
        new BusAlert().addDefaultStyleSheet().setMessage(message).showAndWait();
    }
}
