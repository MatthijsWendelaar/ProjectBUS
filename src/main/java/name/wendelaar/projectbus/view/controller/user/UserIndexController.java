package name.wendelaar.projectbus.view.controller.user;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import name.wendelaar.projectbus.database.concurrency.tasks.SimpleReceiveTask;
import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.manager.IItemManager;
import name.wendelaar.projectbus.util.ChainedLinkedHashMap;
import name.wendelaar.projectbus.view.controller.Controller;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.projectbus.view.parts.TableBuilder;
import name.wendelaar.snowdb.data.DataObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class UserIndexController extends Controller {

    private String title = "Dashboard";

    @FXML
    private AnchorPane showDataPane;
    @FXML
    private Button loanedItemsButton;
    @FXML
    private Button availableItemsButton;
    @FXML
    private Button reservedItemsButton;

    private Node lastClicked = null;

    private TableView<Item> itemView;

    @Override
    public String getTitle() {
        return title;
    }

    @FXML
    private void onShowLoanedItems() {
        if (loanedItemsButton.equals(lastClicked)) {
            return;
        }
        lastClicked = loanedItemsButton;

        ExecutorService service = LlsApi.getController().getExecutorService();
        IItemManager itemManager = LlsApi.getController().getItemManager();

        SimpleReceiveTask<Collection<Item>> receiveItemsTask = new SimpleReceiveTask<Collection<Item>>() {
            @Override
            public Collection<Item> execute() {
                return itemManager.getItemsOfUser(LlsApi.getAuthManager().getCurrentUser());
            }
        };

        receiveItemsTask.setOnSucceeded(t -> {
            itemView.getItems().addAll(receiveItemsTask.getValue());
        });

        service.submit(receiveItemsTask);

        itemView = new TableBuilder<Item, String>().addColumn("Name", "Name")
                .addColumn("Type", "TypeName").addColumn("Date Loaned", "LoanedOutDate")
                .addColumn("Too Late", "ToLateToString").getTableView();

        itemView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY) && !row.isEmpty()) {
                    Item item = row.getItem();

                    SimpleReceiveTask<Collection<ItemAttribute>> receiveAttributesTask = new SimpleReceiveTask<Collection<ItemAttribute>>() {

                        @Override
                        public Collection<ItemAttribute> execute() {
                            return itemManager.getAttributesOfItem(item);
                        }
                    };

                    receiveAttributesTask.setOnSucceeded(t -> {
                        ChainedLinkedHashMap<String, Object> map = new ChainedLinkedHashMap<>();
                        map.add("Id: ", item.getId()).add("Name: ", item.getName()).add("Too Late: ", item.getToLateToString())
                                .add("Type: ", item.getTypeName()).add("Date Loaned: ", item.getLoanedOutDate());

                        for (ItemAttribute attribute : receiveAttributesTask.getValue()) {
                            map.add(attribute.getAttributeName() + ": ", attribute.getAttributeValue());
                        }

                        BusAlert alert = buildAlert(map).addButton(new ButtonType("Return Item", ButtonData.LEFT));

                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if (buttonType.isPresent() && buttonType.get().getButtonData().equals(ButtonData.LEFT)) {
                            service.submit(() -> {
                                itemManager.returnItem(item);
                            });
                            itemView.getItems().remove(item);
                        }
                    });

                    service.submit(receiveAttributesTask);
                }
            });
            return row;
        });

        showDataPane.getChildren().add(itemView);
    }

    @FXML
    private void onShowAvailableItems() {
        if (availableItemsButton.equals(lastClicked)) {
            return;
        }

        lastClicked = availableItemsButton;

        ExecutorService service = LlsApi.getController().getExecutorService();
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

        service.submit(receiveItemsTask);

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
                        service.submit(() -> {
                            LlsApi.getReservationManager().addReservation(user, item);
                        });
                    } else {
                        service.submit(() -> {
                            itemManager.loanOutItem(user, item);
                        });
                    }

                    itemView.getItems().remove(item);
                });

                service.submit(receiveAttributesTask);

            });
            return itemTableRow;
        });

        showDataPane.getChildren().add(itemView);
    }

    @FXML
    private void onShowReservedItems() {
        if (reservedItemsButton.equals(lastClicked)) {
            return;
        }

        ExecutorService service = LlsApi.getController().getExecutorService();
        IItemManager itemManager = LlsApi.getItemManager();
        User user = LlsApi.getAuthManager().getCurrentUser();

        SimpleReceiveTask<Collection<Item>> receiveItemsTask = new SimpleReceiveTask<Collection<Item>>() {
            @Override
            public Collection<Item> execute() {

                return itemManager.requestItems("SELECT item.*, item_type.* FROM reservation INNER JOIN item ON reservation.item_id = item.id INNER JOIN item_type ON item.item_type_id = item_type.id WHERE reservation.user_id = ?", user.getId());
            }
        };

        receiveItemsTask.setOnSucceeded(w -> {
            itemView.getItems().addAll(receiveItemsTask.getValue());
        });

        service.submit(receiveItemsTask);

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
                    alert.addButton(new ButtonType("Cancel Reservation", ButtonData.LEFT));

                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (!buttonType.isPresent() || !buttonType.get().getButtonData().equals(ButtonData.LEFT)) {
                        return;
                    }

                    service.submit(() -> {
                        LlsApi.getReservationManager().removeReservation(user, item);
                    });
                    itemView.getItems().remove(item);
                });

                service.submit(receiveAttributesTask);

            });
            return itemTableRow;
        });

        showDataPane.getChildren().add(itemView);

        lastClicked = reservedItemsButton;


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
