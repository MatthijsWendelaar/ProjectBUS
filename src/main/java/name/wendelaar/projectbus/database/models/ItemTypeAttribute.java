package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.data.model.Model;

public class ItemTypeAttribute extends Model {

    private DataObject attributeObject;

    public ItemTypeAttribute(DataObjectCollection dataObjectCollection) {
        super(dataObjectCollection, "item_type_attribute");
        attributeObject = dataObjectCollection.getDataObjectByTable("attribute");
        if (attributeObject == null) {
            throw new NullPointerException("attribute data is missing");
        }
        if (attributeObject.get("id") != dataObject.get("attribute_id")) {
            throw new IllegalArgumentException("Attribute ids do not match");
        }
    }

    public int getId() {
        return (int) dataObject.get("id");
    }

    public String getAttributeName() {
        return (String) attributeObject.get("name");
    }

    public int getItemTypeId() {
        return (int) dataObject.get("item_type_id");
    }
}
