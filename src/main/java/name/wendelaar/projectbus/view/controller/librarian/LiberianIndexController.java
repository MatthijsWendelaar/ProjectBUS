package name.wendelaar.projectbus.view.controller.librarian;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import name.wendelaar.projectbus.database.concurrency.SimpleReceiveTask;
import name.wendelaar.projectbus.database.manager.IItemManager;
import name.wendelaar.projectbus.database.manager.IUserManager;
import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.models.UserData;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.util.ShowDataAlertBuilder;
import name.wendelaar.projectbus.view.controller.BasicController;
import name.wendelaar.projectbus.view.form.Form;
import name.wendelaar.projectbus.view.form.FormBuilder;
import name.wendelaar.projectbus.view.form.fields.FormComboBox;
import name.wendelaar.projectbus.view.form.validator.AllFilledValidator;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.projectbus.view.parts.TableBuilder;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.SingleDataObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class LiberianIndexController extends BasicController {

    private String title = "Dashboard";

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
        System.out.println("watt");
        //addConstraints(showDataPane);
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

                    BusAlert alert = new ShowDataAlertBuilder().append("UUID", user.getId()).append("Username", user.getUserName())
                            .append("First Name", userData.getFirstName()).append("Last Name", userData.getLastName()).append("Email", user.getEmail())
                            .append("Librarian", user.isLibrarianToString()).append("Account Disabled", user.isAccountDisabledToString())
                            .append("City", userData.getCity()).append("Postal Code", userData.getPostalCode())
                            .append("Address", userData.getStreet() + " " + userData.getHomeNumber()).buildAlert();

                    if (!user.isLibrarian()) {
                        alert.addButton(new ButtonType("Delete User", ButtonData.LEFT));
                        if (user.isAccountDisabled()) {
                            alert.addButton(new ButtonType("Enable Account", ButtonData.LEFT));
                        } else {
                            alert.addButton(new ButtonType("Disable Account", ButtonData.LEFT));
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
                        case "Enable Account":
                        case "Disable Account":
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

        addAndClear(tableView);
        addConstraints(tableView);
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
                new BusAlert().addDefaultIcon().addDefaultStyleSheet().setMessage("The user was successfully added!").showAndWait();
                form.clearForm();
            });

            service.submit(saveUserTask);
        });


        addAndClear(form.getRoot());
        addConstraints(form.getRoot());
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

        addAndClear(tableView);
        addConstraints(tableView);
    }

    @FXML
    private void onLogout() {
        if (isSelected(logoutButton)) {
            return;
        }

        LlsApi.getAuthManager().logout();
    }

    private void addAndClear(Node node) {
        ObservableList<Node> items = showDataPane.getChildren();
        items.clear();
        items.add(node);
    }

    private void addConstraints(Node node) {
        if (node == null) {
            return;
        }

        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setTopAnchor(node, 0.0);
    }
}
