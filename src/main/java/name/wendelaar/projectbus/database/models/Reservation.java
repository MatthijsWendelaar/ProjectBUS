package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.model.Model;

public class Reservation extends Model {

    public Reservation(DataObject dataObject) {
        super(dataObject, "reservation");
    }
}
