package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.Reservation;
import name.wendelaar.projectbus.database.models.User;

import java.util.Collection;

public interface IReservationManager {

    public void addReservation(User user, Item item);

    public void removeReservation(User user, Item item);

    public Collection<Reservation> getReservationsOfItem(Item item);

    public Collection<Reservation> getReservationsOfUser(User user);

    public Collection<Reservation> getReservations();
}
