package name.wendelaar.projectbus.database.models.factory;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.snowdb.data.DataObject;

public interface IReservationFactory {

    public DataObject makeReservation(Item item, User user);

    public DataObject makeReservation(int itemId, int userId);
}
