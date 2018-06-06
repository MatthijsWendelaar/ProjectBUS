package name.wendelaar.projectbus.database.concurrency.tasks;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.main.LlsApi;

import java.util.Collection;

public class SimpleTask extends SimpleReceiveTask<Collection<Item>> {

    @Override
    public Collection<Item> execute() {
        return LlsApi.getItemManager().getItemsOfUser(LlsApi.getAuthManager().getCurrentUser());
    }
}
