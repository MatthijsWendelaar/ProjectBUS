package name.wendelaar.projectbus.view.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import name.wendelaar.projectbus.database.concurrency.DatabaseExecutorWrapper;
import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.concurrency.tasks.SimpleReceiveTask;
import name.wendelaar.projectbus.database.concurrency.tasks.SimpleTask;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.manager.IItemManager;
import name.wendelaar.projectbus.util.ChainedLinkedHashMap;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.projectbus.view.parts.TableBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class IndexController extends Controller {

    private String title = "Home";

    @FXML
    private AnchorPane showDataPane;
    @FXML
    private Button loanedItemsButton;
    @FXML
    private Button availableItemsButton;

    private Node lastClicked = null;

    private TableView<Item> itemView;

    @Override
    public String getTitle() {
        return title;
    }

    @FXML
    private void onShowLoanedItems() {
        if (loanedItemsButton.equals(lastClicked)) {
            System.out.println("Lekker peuh");
            return;
        }
        lastClicked = loanedItemsButton;
        ExecutorService service = LlsApi.getController().getExecutorService();

        SimpleTask simpleTask = new SimpleTask();

        simpleTask.setOnSucceeded(t -> {
            itemView.getItems().addAll(simpleTask.getValue());
            for (Item item : simpleTask.getValue()) {
                System.out.println(item.getId());
                System.out.println(item.getName());
                System.out.println();
            }
        });

        service.submit(simpleTask);

        itemView = new TableBuilder<Item, String>().addColumn("Name", "Name")
                .addColumn("Type", "TypeName").addColumn("Date Loaned", "LoanedOutDate")
                .addColumn("Too Late", "ToLateToString").getTableView();

        itemView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY) && !row.isEmpty()) {
                    Item item = row.getItem();

                    SimpleReceiveTask<Collection<ItemAttribute>> task = new SimpleReceiveTask<Collection<ItemAttribute>>() {

                        @Override
                        public Collection<ItemAttribute> execute() {
                            return LlsApi.getItemManager().getAttributesOfItem(item);
                        }
                    };

                    task.setOnSucceeded(t -> {
                        ChainedLinkedHashMap<String, Object> map = new ChainedLinkedHashMap<>();
                        map.add("Id: ", item.getId()).add("Name: ", item.getName()).add("Too Late: ", item.getToLateToString())
                                .add("Type: ", item.getTypeName()).add("Date Loaned: ", item.getLoanedOutDate());

                        for (ItemAttribute attribute : task.getValue()) {
                            map.add(attribute.getAttributeName() + ": ", attribute.getAttributeValue());
                        }

                        BusAlert alert = buildAlert(map).addButton(new ButtonType("Return Item", ButtonData.LEFT));

                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if (buttonType.isPresent() && buttonType.get().getButtonData().equals(ButtonData.LEFT)) {
                            service.submit(new Runnable() {
                                @Override
                                public void run() {
                                    LlsApi.getItemManager().returnItem(item);
                                }
                            });
                            itemView.getItems().remove(item);
                        }
                    });

                    service.submit(task);
                }
            });
            return row;
        });

        showDataPane.getChildren().add(itemView);
    }

    @FXML
    private void onShowAvailableItems() {
        if (availableItemsButton.equals(lastClicked)) {
            System.out.println("Lekker peuh");
            return;
        }

        lastClicked = availableItemsButton;

        DatabaseExecutorWrapper wrapper = new DatabaseExecutorWrapper(LlsApi.getController().getExecutorService());
        IItemManager itemManager = LlsApi.getItemManager();

        SimpleReceiveTask<Collection<Item>> receiveItemsTask = new SimpleReceiveTask<Collection<Item>>() {
            @Override
            public Collection<Item> execute() {
                return itemManager.getItemsNotOfUser(LlsApi.getAuthManager().getCurrentUser());
            }
        };

        receiveItemsTask.setOnSucceeded(w -> {
            itemView.getItems().addAll(receiveItemsTask.getValue());
        });

        wrapper.submit(receiveItemsTask);

        itemView = new TableBuilder<Item, String>().addColumn("Name", "Name")
                .addColumn("Type", "TypeName").addColumn("Loaned Out", "LoanedOutToString")
                .getTableView();

        itemView.setRowFactory(tv -> {
            TableRow<Item> itemTableRow = new TableRow<>();
            itemTableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() != 2 || !MouseButton.PRIMARY.equals(event.getButton()) || itemTableRow.isEmpty()) {
                    return;
                }

                Item item = itemTableRow.getItem();

                SimpleReceiveTask<Collection<ItemAttribute>> receiveAttributesTask = new SimpleReceiveTask<Collection<ItemAttribute>>() {
                    @Override
                    public Collection<ItemAttribute> execute() {
                        return itemManager.getAttributesOfItem(item);
                    }
                };

                receiveAttributesTask.setOnSucceeded(w -> {

                    ChainedLinkedHashMap<String, Object> map = new ChainedLinkedHashMap<>();

                    map.add("Id: ", item.getId()).add("Name: ", item.getName()).add("Type: ", item.getTypeName())
                            .add("Loaned Out: ", item.getLoanedOutToString());

                    for (ItemAttribute attribute : receiveAttributesTask.getValue()) {
                        map.add(attribute.getAttributeName() + ": ", attribute.getAttributeValue());
                    }

                    BusAlert alert = buildAlert(map);

                    if (item.isLoanedOut()) {
                        alert.addButton(new ButtonType("Reserve", ButtonData.LEFT));
                    } else {
                        alert.addButton(new ButtonType("Loan", ButtonData.LEFT));
                    }

                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (!buttonType.isPresent() || !buttonType.get().getButtonData().equals(ButtonData.LEFT)) {
                        return;
                    }

                    User user = LlsApi.getAuthManager().getCurrentUser();

                    if (item.isLoanedOut()) {
                        wrapper.submitRunnable(() -> {
                            LlsApi.getReservationManager().addReservation(user,item);
                        });
                        LlsApi.getReservationManager().addReservation(user, item);
                    } else {
                        wrapper.submitRunnable(() -> {
                            itemManager.loanOutItem(user, item);
                        });
                    }

                    itemView.getItems().remove(item);
                });

                wrapper.submit(receiveAttributesTask);

            });
            return itemTableRow;
        });

        showDataPane.getChildren().add(itemView);
    }

    private BusAlert buildAlert(Map<String, Object> values) {
        BusAlert busAlert = new BusAlert().addDefaultStyleSheet();
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            builder.append(entry.getKey()).append(entry.getValue()).append("\n");
        }

        busAlert.setMessage(builder.toString());
        return busAlert;
    }
}
