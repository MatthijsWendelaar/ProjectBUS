package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.ItemType;
import name.wendelaar.projectbus.database.models.ItemTypeAttribute;

import java.util.Collection;

public interface IItemAttributeManager {

    public Collection<ItemAttribute> getAttributesOfItem(Item item);

    public Collection<ItemTypeAttribute> getAttributesOfType(ItemType itemType);

    public Collection<ItemTypeAttribute> getAttributesOfType(int id);
}
