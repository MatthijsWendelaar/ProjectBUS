package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemType;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.manager.IItemManager;
import name.wendelaar.projectbus.view.MainManager;

import java.util.Collection;

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
    public void loanOutItem(User borrower, Item item) {

    }

    @Override
    public Collection<Item> getItems() {
        return null;
    }
}
