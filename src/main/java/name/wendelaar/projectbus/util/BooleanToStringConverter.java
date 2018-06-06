package name.wendelaar.projectbus.util;

public class BooleanToStringConverter {

    public static String convert(Object object) {
        if (object instanceof Boolean) {
            return booleanConvert((Boolean) object);
        }
        if (object instanceof Number) {
            return booleanConvert(((Number) object).intValue() != 0);
        }
        return booleanConvert(false);
    }

    private static String booleanConvert(boolean bool) {
        return bool ? "Yes" : "No";
    }
}
