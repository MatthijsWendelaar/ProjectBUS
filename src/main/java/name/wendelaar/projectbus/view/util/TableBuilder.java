package name.wendelaar.projectbus.view.util;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableBuilder<T, S> {

    private TableView<T> tableView;

    public TableBuilder() {
        tableView = new TableView<>();
        tableView.setEditable(false);
        //tableView.setPrefWidth(500);
    }

    public TableBuilder addColumn(String columnName, String propertyName) {
        TableColumn<T, S> tableColumn = new TableColumn<>();
        tableColumn.setText(columnName);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        tableView.getColumns().add(tableColumn);
        return this;
    }

    public TableView<T> getTableView() {
        return tableView;
    }
}
