package name.wendelaar.projectbus.validator;

import name.wendelaar.projectbus.view.parts.BusAlert;

public class InputValidator {

    public static void inputFilled(String message, String... inputs) throws ValidatorException {
        for (String input : inputs) {
            if (input == null || input.trim().equals("")) {
                showAlert(message);
                throw new ValidatorException();
            }
        }
    }

    public static void isTrue(String message, boolean... inputs) throws ValidatorException {
        for (boolean input : inputs) {
            if (!input) {
                showAlert(message);
                throw new ValidatorException();
            }
        }
    }

    private static void showAlert(String message) {
        new BusAlert().addDefaultStyleSheet().setMessage(message).showAndWait();
    }
}
