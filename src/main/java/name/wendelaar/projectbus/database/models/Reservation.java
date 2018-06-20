package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.DataObjectCollection;
import name.wendelaar.snowdb.data.model.Model;

public class Reservation extends Model {

    private DataObject userObject;

    public Reservation(DataObjectCollection dataObject) {
        super(dataObject, "reservation");
        userObject = dataObject.getDataObjectByTable("user");

        if (userObject == null) {
            throw new IllegalArgumentException("reserv");
        }
    }
}
