package name.wendelaar.projectbus.view.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

public class IndexController extends Controller {

    private String title = "Home";

    @FXML
    private Tab loanedItemsTab;

    @FXML
    private TableView itemsTable;

    @Override
    public String getTitle() {
        return title;
    }

    public IndexController() {
        //new BusAlert().addDefaultStyleSheet().setMessage("Welcome " + LlsApi.getAuthManager().getCurrentUser().getUsername()).showAndWait();
    }

    @FXML
    public void getLoanedItems(Event event) {
        if (loanedItemsTab.isSelected()) {
            System.out.println("is het werkelijke zo?");
            System.out.println(event.getSource());
        }

        System.out.println(event.getEventType());
        //System.out.println(event.getSource());
        //System.out.println(event.getTarget());
//        if (event.getSource() == loanedItemsTab) {
//            System.out.println("Ik trigger");
//        }
    }
}
