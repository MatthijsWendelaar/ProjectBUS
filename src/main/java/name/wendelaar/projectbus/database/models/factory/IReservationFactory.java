package name.wendelaar.projectbus.database.models.factory;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.Reservation;
import name.wendelaar.projectbus.database.models.User;

public interface IReservationFactory {

    public Reservation makeReservation(Item item, User user);

    public Reservation makeReservation(int itemId, int userId);
}
