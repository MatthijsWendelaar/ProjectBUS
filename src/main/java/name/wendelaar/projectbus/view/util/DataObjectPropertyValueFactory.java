package name.wendelaar.projectbus.view.util;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import name.wendelaar.snowdb.data.DataObject;

public class DataObjectPropertyValueFactory<T> implements Callback<CellDataFeatures<DataObject, T>, ObservableValue<T>> {

    private String property;
    private DataObjectPropertyReference<T> propertyReference;

    public DataObjectPropertyValueFactory(String property) {
        this.property = property;
    }

    @Override
    public ObservableValue<T> call(CellDataFeatures<DataObject, T> param) {
        if (property == null || property.isEmpty() || param.getValue() == null) return null;

        if (propertyReference == null) {
            propertyReference = new DataObjectPropertyReference<>(property);
        }

        return new ReadOnlyObjectWrapper<>(propertyReference.get(param.getValue()));
    }
}
