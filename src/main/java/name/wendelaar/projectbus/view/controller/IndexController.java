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
import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.projectbus.database.models.Book;
import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.ItemAttribute;
import name.wendelaar.projectbus.view.parts.BusAlert;
import name.wendelaar.projectbus.view.parts.TableBuilder;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.manager.Manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class IndexController extends Controller {

    private String title = "Home";

    @FXML
    private AnchorPane showDataPane;
    @FXML
    private Button loanedItemsButton;

    private Node lastClicked = null;

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
        if (loanedItemsButton.equals(lastClicked)) {
            System.out.println("Lekker peuh");
            return;
        }
        lastClicked = loanedItemsButton;
        Collection<Item> items = LlsApi.getItemManager().getItemsOfUser(LlsApi.getAuthManager().getCurrentUser());

        itemView = new TableBuilder<Item, String>().addColumn("Name", "Name")
                .addColumn("Type", "TypeName").addColumn("Date Loaned", "LoanedOutDate")
                .addColumn("To Late", "ToLateToString").getTableView();

        itemView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY) && !row.isEmpty()) {
                    Item item = row.getItem();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex){}
                    Collection<ItemAttribute> attributes = LlsApi.getItemManager().getAttributesOfItem(item);


                    StringBuilder builder = new StringBuilder();
                    builder.append("ID: ").append(item.getId()).append("\n")
                            .append("Title/Name: ").append(item.getName()).append("\n")
                            .append("Late: ").append(item.getToLateToString()).append("\n")
                            .append("TypeName: ").append(item.getTypeName()).append("\n")
                            .append("Count: ").append(item.getLoanedOutCount()).append("\n");

                    for (ItemAttribute attribute : attributes) {
                        builder.append(attribute.getAttributeName()).append(": ").append(attribute.getAttributeValue()).append("\n");
                    }

                    Optional<ButtonType> buttonType = new BusAlert().addDefaultStyleSheet().setMessage(builder.toString()).addButton(new ButtonType("Return Item", ButtonData.LEFT)).showAndWait();
                    if (buttonType.isPresent() && buttonType.get().getButtonData().equals(ButtonData.LEFT)) {
                        LlsApi.getItemManager().returnItem(item);
                        itemView.getItems().remove(item);
                    }
                }
            });
            return row;
        });

        itemView.getItems().addAll(items);

        showDataPane.getChildren().add(itemView);
    }
}
