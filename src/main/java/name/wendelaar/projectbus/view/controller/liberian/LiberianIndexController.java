package name.wendelaar.projectbus.view.controller.liberian;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import name.wendelaar.projectbus.database.concurrency.SimpleReceiveTask;
import name.wendelaar.projectbus.database.manager.IUserManager;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.main.LlsApi;
import name.wendelaar.projectbus.view.controller.Controller;
import name.wendelaar.projectbus.view.parts.TableBuilder;

import java.util.Collection;
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
        tableView.setEditable(false);

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
