package name.wendelaar.projectbus.view.controller.liberian;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import name.wendelaar.projectbus.database.concurrency.SimpleReceiveTask;
import name.wendelaar.projectbus.database.manager.IUserManager;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.models.UserData;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.util.ShowDataAlertBuilder;
import name.wendelaar.projectbus.view.controller.Controller;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.projectbus.view.parts.TableBuilder;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class LiberianIndexController extends Controller {

    private String title = "Dashboard";

    @FXML
    private AnchorPane showDataPane;
    @FXML
    private Button logoutButton;
    @FXML
    private Button showUsersButton;

    private Node lastClicked = null;

    private TableView tableView;

    public LiberianIndexController() {
        MAX_HEIGHT_SIZE = 450;
        MIN_HEIGHT_SIZE = 430;
        MAX_WIDTH_SIZE = 700;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @FXML
    private void onShowUsers() {
        if (showUsersButton.equals(lastClicked)) {
            return;
        }
        lastClicked = showUsersButton;
        Stage stage = viewManager.getStage();
        stage.setTitle("Users - " + stage.getTitle());

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

                    BusAlert alert = new ShowDataAlertBuilder().append("Id", user.getId()).append("First Name", userData.getFirstName())
                            .append("Last Name", userData.getLastName()).append("Email", user.getEmail())
                            .append("Librarian", user.isLibrarianToString()).append("Account Disabled", user.isAccountDisabledToString())
                            .append("City", userData.getCity()).append("Postal Code", userData.getPostalCode())
                            .append("Street", userData.getStreet()).append("Home Number", userData.getHomeNumber()).buildAlert();

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

        showDataPane.getChildren().add(tableView);
    }

    @FXML
    private void onLogout() {
        if (logoutButton.equals(lastClicked)) {
            return;
        }
        lastClicked = logoutButton;

        LlsApi.getAuthManager().logout();
    }
}
