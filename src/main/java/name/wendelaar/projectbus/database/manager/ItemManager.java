package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.ItemType;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.manager.IItemManager;
import name.wendelaar.projectbus.main.MainManager;
import name.wendelaar.simplevalidator.BoolValidator;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.manager.Manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public class ItemManager implements IItemManager {

    private MainManager mainManager;

    public ItemManager(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void addItem(String itemName, ItemType type) {

    }

    @Override
    public void removeItem(Item item) {

    }

    @Override
    public void returnItem(Item item) {
        item.resetItem();
        Manager.saveModel(item);
    }

    @Override
    public void loanOutItem(User borrower, Item item) {
        if (!BoolValidator.notNull(borrower, item)) {
            return;
        }
        if (borrower.getId() == 0 || item.getId() == 0) {
            return;
        }

        item.loanOutItem(borrower);
        item.printInfo();
        Manager.saveModel(item);
    }

    @Override
    public Collection<Item> getItemsOfUser(User user) {
        if (user == null || user.getId() == 0) {
            return null;
        }

        return requestItems("SELECT * FROM item INNER JOIN item_type ON item.item_type_id = item_type.id WHERE item.user_id = ?", user.getId());
    }

    @Override
    public Collection<Item> getItemsNotOfUser(User user) {
        if (user == null || user.getId() == 0) {
            return null;
        }
        return requestItems("SELECT * FROM item INNER JOIN item_type ON item.item_type_id = item_type.id WHERE item.user_id <> ? OR item.user_id IS NULL", user.getId());
    }

    @Override
    public Collection<ItemAttribute> getAttributesOfItem(Item item) {
        List<ItemAttribute> itemAttributes = new ArrayList<>();
        if (item == null || item.getId() == 0){
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
            return itemAttributes;
        }
        return itemAttributes;
    }

    @Override
    public Collection<Item> getItems() {
        return null;
    }

    private Collection<Item> requestItems(String query, Object... objects) {
        List<Item> items = new ArrayList<>();

        try {
            Manager manager = Manager.create().prepare(query);

            for (Object o : objects) {
                manager.setValue(o);
            }
            List<DataObject> dataObjects = manager.find();

            for (DataObject dataObject : dataObjects) {
                items.add(new Item((DataObjectCollection) dataObject, "all"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return items;
        }
        return items;
    }
}
