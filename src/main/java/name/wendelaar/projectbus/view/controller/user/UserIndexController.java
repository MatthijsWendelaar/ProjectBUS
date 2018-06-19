package name.wendelaar.projectbus.view.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import name.wendelaar.projectbus.database.concurrency.SimpleReceiveTask;
import name.wendelaar.projectbus.database.manager.IItemManager;
import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.view.util.InfoAlertBuilder;
import name.wendelaar.projectbus.view.controller.AbstractDashboardController;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.projectbus.view.parts.TableBuilder;
import name.wendelaar.projectbus.view.util.PaneHelper;
import name.wendelaar.projectbus.view.util.ViewUtil;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class UserIndexController extends AbstractDashboardController {

    private String title = "Dashboard";
    private PaneHelper helper;

    @FXML
    private AnchorPane showDataPane;
    @FXML
    private Button loanedItemsButton;
    @FXML
    private Button availableItemsButton;
    @FXML
    private Button reservedItemsButton;
    @FXML
    private Button logoutButton;

    private TableView<Item> itemView;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setupAfterInitialization() {
        helper = new PaneHelper(showDataPane);
        onShowLoanedItems();
    }

    @FXML
    private void onShowLoanedItems() {
        if (isSelected(loanedItemsButton)) {
            return;
        }

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

        itemView = new TableBuilder<Item, String>().addColumn("Name", "Name")
                .addColumn("Type", "TypeName").addColumn("Date Loaned", "LoanedOutDate")
                .addColumn("Too Late", "ToLateToString").getTableView();

        service.submit(receiveItemsTask);

        itemView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY) && !row.isEmpty()) {
                    Item item = row.getItem();

                    SimpleReceiveTask<Collection<ItemAttribute>> receiveAttributesTask = new SimpleReceiveTask<Collection<ItemAttribute>>() {

                        @Override
                        public Collection<ItemAttribute> execute() {
                            return LlsApi.getItemAttributeManager().getAttributesOfItem(item);
                        }
                    };

                    receiveAttributesTask.setOnSucceeded(t -> {
                        InfoAlertBuilder alertBuilder = new InfoAlertBuilder().appendLine("Id",  item.getId())
                                .appendLine("Name", item.getName()).appendLine("Too Late", item.getToLateToString())
                                .appendLine("Type", item.getTypeName()).appendLine("Date Loaned", item.getLoanedOutDate());

                        for (ItemAttribute attribute : receiveAttributesTask.getValue()) {
                            alertBuilder.appendLine(attribute.getAttributeName(), attribute.getAttributeValue());
                        }

                        BusAlert alert = alertBuilder.buildAlert().addButton(new ButtonType("Return Item", ButtonData.LEFT));

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

        addTableToView();
    }

    @FXML
    private void onShowAvailableItems() {
        if (isSelected(availableItemsButton)) {
            return;
        }

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
                        return LlsApi.getItemAttributeManager().getAttributesOfItem(item);
                    }
                };

                receiveAttributesTask.setOnSucceeded(w -> {

                    InfoAlertBuilder alertBuilder = new InfoAlertBuilder().appendLine("Id", item.getId()).appendLine("Name", item.getName()).appendLine("Type", item.getTypeName())
                            .appendLine("Loaned Out", item.getLoanedOutToString());

                    for (ItemAttribute attribute : receiveAttributesTask.getValue()) {
                        alertBuilder.appendLine(attribute.getAttributeName(), attribute.getAttributeName());
                    }

                    BusAlert alert = alertBuilder.buildAlert();

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

        addTableToView();
    }

    @FXML
    private void onShowReservedItems() {
        if (isSelected(reservedItemsButton)) {
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
                        return LlsApi.getItemAttributeManager().getAttributesOfItem(item);
                    }
                };

                receiveAttributesTask.setOnSucceeded(w -> {

                    InfoAlertBuilder alertBuilder = new InfoAlertBuilder().appendLine("Name", item.getName())
                            .appendLine("Type", item.getTypeName()).appendLine("Loaned Out",item.getLoanedOutToString());

                    for (ItemAttribute attribute : receiveAttributesTask.getValue()) {
                        alertBuilder.appendLine(attribute.getAttributeName(), attribute.getAttributeValue());
                    }

                    BusAlert alert = alertBuilder.buildAlert().addButton(new ButtonType("Cancel Reservation", ButtonData.LEFT));

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

        addTableToView();
    }

    @FXML
    private void onLogout() {
        if (isSelected(logoutButton)) {
            return;
        }

        LlsApi.getAuthManager().logout();
    }

    private void addTableToView() {
        helper.clearAndAdd(itemView);
        ViewUtil.addConstraints(itemView);
    }
}
