package name.wendelaar.projectbus.database.models;

import name.wendelaar.simplevalidator.ExcepValidator;
import name.wendelaar.snowdb.annotation.Data;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.data.model.Model;

public class ItemAttribute extends Model {

    @Data
    private DataObject itemTypeAttribute;
    @Data
    private DataObject attribute;

    public ItemAttribute(DataObjectCollection collection) {
        super(collection, "item_attribute_values");

        itemTypeAttribute = collection.getDataObjectByTable("item_type_attribute");
        ExcepValidator.notNull("Item type attribute not found", itemTypeAttribute);

        attribute = collection.getDataObjectByTable("attribute");
        ExcepValidator.notNull("Attribute not found",attribute);
    }

    public String getAttributeName() {
        return (String) attribute.get("name");
    }

    public Object getAttributeValue() {
        return dataObject.get("value");
    }

    public void setAttributeValue(Object value) {
        dataObject.set("value", value);
    }
}
