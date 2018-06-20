package name.wendelaar.projectbus.view.util;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import name.wendelaar.snowdb.data.DataObject;

public class DataObjectTableBuilder {

    private TableView<DataObject> tableView;

    public DataObjectTableBuilder() {
        tableView = new TableView<>();
        tableView.setEditable(false);
    }

    public DataObjectTableBuilder addColumn(String columnName, String propertyName) {
        TableColumn<DataObject,String> tableColumn = new TableColumn<>();
        tableColumn.setText(columnName);
        tableColumn.setCellValueFactory(new DataObjectPropertyValueFactory<>(propertyName));
        tableView.getColumns().add(tableColumn);
        return this;
    }

    public TableView<DataObject> buildTable() {
        return tableView;

    }
}
