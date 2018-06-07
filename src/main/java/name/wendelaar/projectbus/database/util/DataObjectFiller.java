package name.wendelaar.projectbus.database.util;

import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.SingleDataObject;

import java.util.HashMap;
import java.util.Map;

public class DataObjectFiller {

    private String tableName;
    private Map<String, Object> dataMap = new HashMap<>();


    public DataObjectFiller(String tableName) {
        this.tableName = tableName;
    }

    public DataObjectFiller add(String key, Object value) {
        String fullName = tableName + "." + key;
        if (dataMap.containsKey(fullName)) {
            dataMap.replace(fullName, value);
        } else {
            dataMap.put(fullName, value);
        }
        return this;
    }

    public DataObject build() {
        Map<String, Object> dataObjectMap = new HashMap<>();

        for (String s : dataMap.keySet()) {
            dataObjectMap.put(s, null);
        }
        DataObject dataObject = new SingleDataObject(dataObjectMap, tableName);

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            dataObject.set(entry.getKey(), entry.getValue());
        }
        return dataObject;
    }
}
