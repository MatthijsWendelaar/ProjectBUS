package name.wendelaar.projectbus.view.controller.librarian;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import name.wendelaar.projectbus.database.concurrency.SimpleReceiveTask;
import name.wendelaar.projectbus.database.manager.IItemManager;
import name.wendelaar.projectbus.database.manager.IUserManager;
import name.wendelaar.projectbus.database.models.*;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.view.util.InfoAlertBuilder;
import name.wendelaar.projectbus.view.controller.AbstractDashboardController;
import name.wendelaar.projectbus.view.form.Form;
import name.wendelaar.projectbus.view.form.FormBuilder;
import name.wendelaar.projectbus.view.form.fields.FormComboBox;
import name.wendelaar.projectbus.view.form.validator.AllFilledValidator;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.projectbus.view.parts.TableBuilder;
import name.wendelaar.projectbus.view.util.PaneHelper;
import name.wendelaar.projectbus.view.util.ViewUtil;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.SingleDataObject;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class LiberianIndexController extends AbstractDashboardController {

    private String title = "Dashboard";
    private PaneHelper paneHelper;

    @FXML
    private AnchorPane showDataPane;
    @FXML
    private Button logoutButton;
    @FXML
    private Button showUsersButton;
    @FXML
    private Button createUserButton;
    @FXML
    private Button showItemsButton;
    @FXML
    private Button createItemButton;

    private TableView tableView;

    public LiberianIndexController() {
        MAX_HEIGHT_SIZE = 430;
        MIN_HEIGHT_SIZE = 430;
        MAX_WIDTH_SIZE = 700;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setupAfterInitialization() {
        paneHelper = new PaneHelper(showDataPane);
        onShowUsers();
    }

    @FXML
    private void onShowUsers() {
        if (isSelected(showUsersButton)) {
            return;
        }

        ExecutorService service = LlsApi.getController().getExecutorService();
        IUserManager userManager = LlsApi.getUserManager();

        SimpleReceiveTask<Collection<User>> receiveUsersTask = new SimpleReceiveTask<Collection<User>>() {
            @Override
            public Collection<User> execute() {
                return userManager.getUsersExceptOne(LlsApi.getAuthManager().getCurrentUser());
            }
        };

        receiveUsersTask.setOnSucceeded(wt -> {
            tableView.getItems().addAll(receiveUsersTask.getValue());
        });

        tableView = new TableBuilder<User, String>().addColumn("Id", "Id").addColumn("Username", "UserName")
                .addColumn("Email", "Email").addColumn("Librarian", "LibrarianToString")
                .addColumn("Account Disabled", "AccountDisabledToString").getTableView();

        tableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() != 2 || !event.getButton().equals(MouseButton.PRIMARY) || row.isEmpty()) {
                    return;
                }

                User user = row.getItem();
                SimpleReceiveTask<UserData> receiveUserDataPersonalTask = new SimpleReceiveTask<UserData>() {

                    @Override
                    public UserData execute() {
                        return userManager.getUserData(user.getId());
                    }
                };

                receiveUserDataPersonalTask.setOnSucceeded(t -> {
                    UserData userData = receiveUserDataPersonalTask.getValue();

                    BusAlert alert = new InfoAlertBuilder().appendLine("UUID", user.getId()).appendLine("Username", user.getUserName())
                            .appendLine("First Name", userData.getFirstName()).appendLine("Last Name", userData.getLastName()).appendLine("Email", user.getEmail())
                            .appendLine("Librarian", user.isLibrarianToString()).appendLine("Account Disabled", user.isAccountDisabledToString())
                            .appendLine("City", userData.getCity()).appendLine("Postal Code", userData.getPostalCode())
                            .appendLine("Address", userData.getStreet() + " " + userData.getHomeNumber()).buildAlert();

                    if (!user.isLibrarian()) {
                        alert.addButton(new ButtonType("Delete User", ButtonData.LEFT));
                        if (user.isAccountDisabled()) {
                            alert.addButton(new ButtonType("Enable", ButtonData.LEFT));
                        } else {
                            alert.addButton(new ButtonType("Disable", ButtonData.LEFT));
                        }
                    }

                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (user.isLibrarian() || !buttonType.isPresent() || !buttonType.get().getButtonData().equals(ButtonData.LEFT)) {
                        return;
                    }

                    switch (buttonType.get().getText()) {
                        case "Delete User":
                            service.submit(() -> {
                                LlsApi.getUserManager().deleteUser(user);
                            });
                            tableView.getItems().remove(user);
                            break;
                        case "Enable":
                        case "Disable":
                            service.submit(() -> {
                                LlsApi.getUserManager().disableUser(user);
                            });
                            tableView.refresh();
                    }
                });

                service.submit(receiveUserDataPersonalTask);

            });
            return row;
        });

        service.submit(receiveUsersTask);

        paneHelper.clearAndAdd(tableView);
        ViewUtil.addConstraints(tableView);
    }

    @FXML
    private void onShowCreateUser() {
        if (isSelected(createUserButton)) {
            return;
        }

        FormBuilder builder = new FormBuilder();
        ExecutorService service = LlsApi.getController().getExecutorService();

        HashMap<String, Object> comboBoxMap = new HashMap<String, Object>() {
            {
                put("Librarian", 1);
                put("Default User", 0);
            }
        };
        FormComboBox<String> formComboBox = new FormComboBox<>(comboBoxMap);

        builder.addTextField("Username", "user.username").addTextField("Email", "user.email").addPasswordField("Password", "user.password")
                .addTextField("First Name", "user_data_personal.first_name").addTextField("Last Name", "user_data_personal.last_name")
                .addComboBox("Role", "user.rank", formComboBox).addDateField("Birth Date", "user_data_personal.birth_date")
                .addTextField("City", "user_data_personal.city").addTextField("Street", "user_data_personal.street")
                .addTextField("Postal Code", "user_data_personal.postal_code").addTextField("Home Number", "user_data_personal.home_number")
                .addValidator(new AllFilledValidator());

        Form form = builder.build();

        form.setReceiver(map -> {
            DataObject userObject = new SingleDataObject(new HashMap<>(), "user");
            DataObject userDataObject = new SingleDataObject(new HashMap<>(), "user_data_personal");

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                if (key.startsWith("user_data")) {
                    userDataObject.set(key, value);
                } else {
                    userObject.set(key, value);
                }
            }
            SimpleReceiveTask<Void> saveUserTask = new SimpleReceiveTask<Void>() {
                @Override
                public Void execute() {
                    LlsApi.getUserManager().createUser(userObject, userDataObject);
                    return null;
                }
            };

            saveUserTask.setOnSucceeded(wk -> {
                form.clearForm();
                new BusAlert().addDefaultIcon().addDefaultStyleSheet().setMessage("The user was successfully added!").showAndWait();
            });

            saveUserTask.setOnFailed(wk -> {
                form.clearForm();
                new BusAlert().addDefaultIcon().addDefaultStyleSheet().setMessage("Something went wrong while adding the user, please try again").showAndWait();
            });

            service.submit(saveUserTask);
        });


        paneHelper.clearAndAdd(form.getRoot());
        ViewUtil.addConstraints(form.getRoot());
    }

    @FXML
    private void onShowItems() {
        if (isSelected(showItemsButton)) {
            return;
        }

        ExecutorService service = LlsApi.getController().getExecutorService();
        IItemManager itemManager = LlsApi.getItemManager();

        SimpleReceiveTask<Collection<Item>> receiveItemsTask = new SimpleReceiveTask<Collection<Item>>() {
            @Override
            public Collection<Item> execute() {
                return itemManager.getItems();
            }
        };

        receiveItemsTask.setOnSucceeded(w -> {
            tableView.getItems().addAll(receiveItemsTask.getValue());
        });

        service.submit(receiveItemsTask);

        tableView = new TableBuilder<Item, String>().addColumn("Name", "Name")
                .addColumn("Type", "TypeName").addColumn("Loaned Out", "LoanedOutToString")
                .addColumn("Loaned Out At", "LoanedOutDate")
                .getTableView();

        tableView.setRowFactory(table -> {
            TableRow<Item> tableRow = new TableRow<>();

            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() != 2 || !MouseButton.PRIMARY.equals(event.getButton()) || tableRow.isEmpty()) {
                    return;
                }

                Item item = tableRow.getItem();

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

                    BusAlert alert = alertBuilder.buildAlert().addButton(new ButtonType("Remove Item", ButtonData.LEFT));

                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.isPresent() && buttonType.get().getButtonData().equals(ButtonData.LEFT)) {
                        service.submit(() -> {
                            itemManager.removeItem(item);
                        });
                        tableView.getItems().remove(item);
                    }
                });

                service.submit(receiveAttributesTask);
            });

            return tableRow;
        });

        paneHelper.clearAndAdd(tableView);
        ViewUtil.addConstraints(tableView);
    }

    @FXML
    private void onCreateItem() {
        if (isSelected(createItemButton)) {
            return;
        }

        ExecutorService service = LlsApi.getController().getExecutorService();

        SimpleReceiveTask<Collection<ItemType>> itemTypeReceiveTask = new SimpleReceiveTask<Collection<ItemType>>() {
            @Override
            public Collection<ItemType> execute() {
                return LlsApi.getItemManager().getItemTypes();
            }
        };

        itemTypeReceiveTask.setOnSucceeded(wk -> {
            HashMap<String, Integer> comboBoxMap = new LinkedHashMap<>();

            Collection<ItemType> itemTypes = itemTypeReceiveTask.getValue();

            for (ItemType itemType : itemTypes) {
                comboBoxMap.put(itemType.getName(), itemType.getId());
            }

            ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(comboBoxMap.keySet()));

            comboBox.setOnAction(event -> {
                String selected = comboBox.getSelectionModel().getSelectedItem();
                int id = comboBoxMap.get(selected);

                setItemForm(id);
                //new InfoAlertBuilder().appendLine(selected, id).buildAlert().showAndWait();
            });

            paneHelper.clearAndAdd(comboBox);

            comboBox.getSelectionModel().selectFirst();
            comboBox.fireEvent(new ActionEvent());
        });

        service.submit(itemTypeReceiveTask);
    }

    @FXML
    private void onLogout() {
        if (isSelected(logoutButton)) {
            return;
        }

        LlsApi.getAuthManager().logout();
    }

    private void setItemForm(int itemType) {
        ExecutorService service = LlsApi.getController().getExecutorService();

        SimpleReceiveTask<Collection<ItemTypeAttribute>> itemAttributesReceiveTask = new SimpleReceiveTask<Collection<ItemTypeAttribute>>() {
            @Override
            public Collection<ItemTypeAttribute> execute() {
                return LlsApi.getItemAttributeManager().getAttributesOfType(itemType);
            }
        };

        itemAttributesReceiveTask.setOnSucceeded(wk -> {
            Collection<ItemTypeAttribute> attributes = itemAttributesReceiveTask.getValue();

            FormBuilder builder = new FormBuilder().addTextField("Item Name", "item.item_name").addValidator(new AllFilledValidator());

            for (ItemTypeAttribute attribute : attributes) {
                builder.addTextField(attribute.getAttributeName(), String.valueOf(attribute.getId()));
            }

            Form form = builder.build();
            form.setReceiver(new CreateItemReceiver(itemType, form));

            paneHelper.addItem(form.getRoot());
            ViewUtil.addConstraints(form.getRoot(), 30,0,0,0);
        });

        service.submit(itemAttributesReceiveTask);
    }
}
