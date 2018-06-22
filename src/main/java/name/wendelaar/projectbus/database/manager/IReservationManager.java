package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.User;

public interface IReservationManager {

    public void addReservation(User user, Item item);

    public void removeReservation(User user, Item item);
}
