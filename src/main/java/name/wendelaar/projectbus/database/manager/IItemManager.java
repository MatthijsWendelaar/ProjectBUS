package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.ItemType;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.snowdb.data.DataObject;

import java.util.Collection;

public interface IItemManager {

    public void addItem(DataObject item, Collection<DataObject> attributes);

    public void removeItem(Item item);

    public void loanOutItem(User borrower, Item item);

    public void returnItem(Item item);

    public Collection<Item> getItemsOfUser(User user);

    public Collection<Item> getItemsNotOfUser(User user);

    public Collection<Item> requestItems(String query, Object... objects);

    public Collection<Item> getItems();

    public Collection<ItemType> getItemTypes();
}
