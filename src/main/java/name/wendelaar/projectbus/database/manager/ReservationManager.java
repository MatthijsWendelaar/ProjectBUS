package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.Reservation;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.models.factory.IReservationFactory;
import name.wendelaar.projectbus.database.models.factory.ReservationFactory;
import name.wendelaar.projectbus.main.MainManager;
import name.wendelaar.projectbus.manager.IReservationManager;
import name.wendelaar.simplevalidator.BoolValidator;
import name.wendelaar.snowdb.manager.Manager;

import java.sql.SQLException;
import java.util.Collection;

public class ReservationManager implements IReservationManager {

    private MainManager mainManager;
    private IReservationFactory reservationFactory;

    public ReservationManager(MainManager mainManager) {
        this.mainManager = mainManager;
        reservationFactory = new ReservationFactory();
    }

    @Override
    public void addReservation(User user, Item item) {
        Reservation reservation = reservationFactory.makeReservation(item, user);
        System.out.println("DERP");
        if (reservation != null) {
            System.out.println("Lekkar");
            Manager.saveModel(reservation);
        }
    }

    @Override
    public void removeReservation(User user, Item item) {
        Manager manager = null;
        if (!BoolValidator.notNull(user, item)) {
            return;
        }
        if (user.getId() == 0 || item.getId() == 0) {
            return;
        }

        try {
            manager = Manager.create().prepare("DELETE FROM reservation WHERE reservation.user_id = ? AND reservation.item_id = ?", user.getId(), item.getId());

            manager.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (manager != null) {
                manager.saveClose();
            }
        }
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
