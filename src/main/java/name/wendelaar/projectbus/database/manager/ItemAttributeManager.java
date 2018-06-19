package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.ItemType;
import name.wendelaar.projectbus.database.models.ItemTypeAttribute;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.manager.Manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemAttributeManager implements IItemAttributeManager {

    @Override
    public Collection<ItemAttribute> getAttributesOfItem(Item item) {
        List<ItemAttribute> itemAttributes = new ArrayList<>();
        if (item == null || item.getId() == 0) {
            return itemAttributes;
        }

        try {
            List<DataObject> dataObjects = Manager.create().prepare("SELECT * FROM item_attribute_values INNER JOIN item_type_attribute ON item_attribute_values.item_type_attribute_id = item_type_attribute.id INNER JOIN attribute ON item_type_attribute.attribute_id = attribute.id WHERE item_attribute_values.item_id = ?")
                    .setValue(item.getId())
                    .find();
            for (DataObject dataObject : dataObjects) {
                itemAttributes.add(new ItemAttribute((DataObjectCollection) dataObject));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return itemAttributes;
    }

    @Override
    public Collection<ItemTypeAttribute> getAttributesOfType(ItemType itemType) {
        int id;
        if (itemType == null || (id = itemType.getId()) == 0) {
            return null;
        }

        return getAttributesOfType(id);
    }

    @Override
    public Collection<ItemTypeAttribute> getAttributesOfType(int id) {
        List<ItemTypeAttribute> attributes = new ArrayList<>();

        try {
            List<DataObject> attributeObjects = Manager.create().prepare("SELECT * FROM item_type_attribute INNER JOIN attribute ON attribute.id = item_type_attribute.attribute_id WHERE item_type_attribute.item_type_id = ?", id).find();

            for (DataObject attributeObject : attributeObjects) {
                attributes.add(new ItemTypeAttribute((DataObjectCollection) attributeObject));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return attributes;
    }
}
