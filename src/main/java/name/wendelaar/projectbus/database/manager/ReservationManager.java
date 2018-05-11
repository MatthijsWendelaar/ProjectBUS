package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.Reservation;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.manager.IReservationManager;
import name.wendelaar.projectbus.view.MainManager;

import java.util.Collection;

public class ReservationManager implements IReservationManager {

    private MainManager mainManager;

    public ReservationManager(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void addReservation(User user, Item item) {

    }

    @Override
    public void removeReservation(User user, Item item) {

    }

    @Override
    public Collection<Reservation> getReservationsOfItem(Item item) {
        return null;
    }

    @Override
    public Collection<Reservation> getReservationsOfUser(User user) {
        return null;
    }

    @Override
    public Collection<Reservation> getReservations() {
        return null;
    }
}
