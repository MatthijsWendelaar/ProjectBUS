package name.wendelaar.projectbus.view.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.projectbus.database.models.Book;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.models.UserData;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.manager.Manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IndexController extends Controller {

    private String title = "Home";

    @FXML
    private Tab loanedItemsTab;

    @FXML
    private TableView<User> itemsTable;

    @FXML
    private TableColumn<User, String> name;

    @FXML
    private TableColumn<User, Integer> age;

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
        //UserData data = LlsApi.getUserManager().getUserData(1);
        //data.printAll();
        System.out.println();
        //User user = LlsApi.getUserManager().getUser(1);
        //user.printAll();

        try {
            DataObject dataObject = Manager.create().prepare("SELECT * FROM item INNER JOIN item_type ON item.item_type_id = item_type.id WHERE item_type.id = ? LIMIT 1")
                    .setValue(1)
                    .findOne();
            Book book = new Book((DataObjectCollection) dataObject);
            List<DataObject> dataObjects = Manager.create().prepare("SELECT * FROM item_attribute_values INNER JOIN item_type_attribute ON item_attribute_values.item_type_attribute_id = item_type_attribute.id INNER JOIN attribute ON item_type_attribute.attribute_id = attribute.id WHERE item_attribute_values.item_id = ?")
                    .setValue(book.getId())
                    .find();

            List<ItemAttribute> itemAttributes = new ArrayList<>();

            for (DataObject dataObject2 : dataObjects) {
                ItemAttribute attribute = new ItemAttribute((DataObjectCollection) dataObject2);
                itemAttributes.add(attribute);
            }

            book.setAttributes(itemAttributes);

            System.out.println("Author: " + book.getAuthor());
            System.out.println("ISBN: " + book.getISBN());
            System.out.println("Title/Name: " + book.getName());
            System.out.println("Loaned out count: " + book.getLoanedOutCount());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        name.setCellValueFactory(new PropertyValueFactory<User, String>("Email"));
        age.setCellValueFactory(new PropertyValueFactory<User, Integer>("Id"));

        Collection<User> users = LlsApi.getUserManager().getUsers();

        itemsTable.getItems().addAll(users);
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
