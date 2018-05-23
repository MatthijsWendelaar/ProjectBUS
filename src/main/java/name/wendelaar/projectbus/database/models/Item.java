package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.model.Model;

public class Item extends Model {

    public Item(DataObject dataObject) {
        super(dataObject, "item");
    }
}
