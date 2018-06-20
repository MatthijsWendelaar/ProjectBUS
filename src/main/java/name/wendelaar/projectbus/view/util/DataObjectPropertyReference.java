package name.wendelaar.projectbus.view.util;

import name.wendelaar.snowdb.data.DataObject;
import sun.reflect.misc.MethodUtil;

import java.lang.reflect.Method;

public class DataObjectPropertyReference<T> {

    private String name;
    private Method getter;

    public DataObjectPropertyReference(String name) {
        this.name = name;
        check();
    }

    public T get(DataObject dataObject) {
        try {
            return (T)MethodUtil.invoke(getter, dataObject, new Object[]{ name});
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void check() {
        for (Method method : DataObject.class.getMethods()) {
            if ("get".equals(method.getName())) {
                getter = method;
                break;
            }
        }
    }
}
