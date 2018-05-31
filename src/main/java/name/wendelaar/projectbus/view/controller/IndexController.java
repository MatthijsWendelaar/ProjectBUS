package name.wendelaar.projectbus.view.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.projectbus.database.models.*;
import name.wendelaar.projectbus.view.parts.TableBuilder;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.manager.Manager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IndexController extends Controller {

    private String title = "Home";

    @FXML
    private AnchorPane showDataPane;

    private TableView itemView;

    @Override
    public String getTitle() {
        return title;
    }

    public IndexController() {
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
    private void onShowLoanedItems() {
        try {
            List<DataObject> dataObjects = Manager.create().prepare("SELECT * FROM item INNER JOIN item_type ON item.item_type_id = item_type.id WHERE item.user_id = ?")
                    .setValue(LlsApi.getAuthManager().getCurrentUser().getId())
                    .find();

            List<Item> items = new ArrayList<>();

            for (DataObject dataObject : dataObjects) {
                items.add(new Item((DataObjectCollection) dataObject, "all"));
            }

            TableBuilder<Item,String> builder = new TableBuilder<>();
            itemView = builder.addColumn("Name", "Name")
                    .addColumn("Type", "TypeName").addColumn("Date Loaned", "LoanedOutDate")
                    .addColumn("To Late", "ToLateToString").getTableView();
            itemView.getItems().addAll(items);
            showDataPane.getChildren().add(itemView);

        } catch (SQLException ex) {ex.printStackTrace();}
    }
}
