package name.wendelaar.projectbus.view.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.models.UserData;

public class IndexController extends Controller {

    private String title = "Home";

    @FXML
    private Tab loanedItemsTab;

    @FXML
    private TableView<PersonModel> itemsTable;

    @FXML
    private TableColumn<PersonModel, String> name;

    @FXML
    private TableColumn<PersonModel, Integer> age;

    @FXML
    private ComboBox<String> combo;

    protected static final String[] names = {"Jan", "Piet", "Klaas", "Nathan", "Ellen"};
    protected static final int[] ages = {10, 21, 58, 20, 32};

    @Override
    public String getTitle() {
        return title;
    }

    public IndexController() {
        //new BusAlert().addDefaultStyleSheet().setMessage("Welcome " + LlsApi.getAuthManager().getCurrentUser().getUsername()).showAndWait();
        UserData data = LlsApi.getUserManager().getUserData(1);
        data.printAll();
        System.out.println( );
        User user = LlsApi.getUserManager().getUser(1);
        user.printAll();
    }

    @FXML
    public void getLoanedItems(Event event) {
        if (loanedItemsTab.isSelected()) {
        }

        //System.out.println(event.getEventType());
        //System.out.println(event.getSource());
        //System.out.println(event.getTarget());
//        if (event.getSource() == loanedItemsTab) {
//            System.out.println("Ik trigger");
//        }
    }

    @FXML
    void initialize() {
        combo.getItems().addAll("Laat ze allemaal zien", "Laat jonger dan 40 zien", "Laat 40 jaar en ouder zien");
        combo.getSelectionModel().selectFirst();

        name.setCellValueFactory(new PropertyValueFactory<PersonModel, String>("name"));
        age.setCellValueFactory(new PropertyValueFactory<PersonModel, Integer>("Age"));

        itemsTable.getItems().add(new PersonModel("Tijs", 18));
    }

    public class PersonModel {
        private String name;
        private int age;

        public PersonModel(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
