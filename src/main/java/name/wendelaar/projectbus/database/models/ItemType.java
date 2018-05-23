package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.model.Model;

public class ItemType extends Model {

    public ItemType(DataObject dataObject) {
        super(dataObject, "item_type");
    }

    public int getId() {
        return (int) dataObject.get("id");
    }

    public String getName() {
        return (String) dataObject.get("type_name");
    }
}
