package name.wendelaar.projectbus.view.controller.librarian;

import name.wendelaar.projectbus.database.concurrency.SimpleReceiveTask;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.view.form.Form;
import name.wendelaar.projectbus.view.form.IFormReceiver;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.SingleDataObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class CreateItemReceiver implements IFormReceiver {

    private int itemTypeId;
    private Form form;

    public CreateItemReceiver(int itemTypeId, Form form) {
        this.itemTypeId = itemTypeId;
        this.form = form;
    }

    @Override
    public void receive(Map<String, Object> data) {
        ExecutorService service = LlsApi.getController().getExecutorService();

        List<DataObject> attributeObjects = new ArrayList<>();
        DataObject itemDataObject = new SingleDataObject(new HashMap<>(), "item");

        itemDataObject.set("item.item_type_id", itemTypeId);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();

            if (key.startsWith("item")) {
                itemDataObject.set(key, entry.getValue());
            } else {
                DataObject attributeObject = new SingleDataObject(new HashMap<>(), "item_attribute_values");

                attributeObject.set("item_attribute_values.item_type_attribute_id", key);
                attributeObject.set("item_attribute_values.value", entry.getValue());

                attributeObjects.add(attributeObject);
            }
        }

        SimpleReceiveTask<Void> saveItemTask = new SimpleReceiveTask<Void>() {
            @Override
            public Void execute() {
                LlsApi.getItemManager().addItem(itemDataObject, attributeObjects);
                return null;
            }
        };

        saveItemTask.setOnSucceeded(event -> {
            form.clearForm();
            new BusAlert().addDefaultIcon().addDefaultStyleSheet().setMessage("The item was successfully added!").showAndWait();
        });

        saveItemTask.setOnFailed(event -> {
            form.clearForm();
            new BusAlert().addDefaultIcon().addDefaultStyleSheet().setMessage("Something went wrong while adding the item, please try again").showAndWait();
        });

        service.submit(saveItemTask);
    }
}
