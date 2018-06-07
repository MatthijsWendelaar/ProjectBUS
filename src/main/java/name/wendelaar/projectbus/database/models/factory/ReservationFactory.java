package name.wendelaar.projectbus.database.models.factory;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.Reservation;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.util.DataObjectFiller;
import name.wendelaar.simplevalidator.BoolValidator;

public class ReservationFactory implements IReservationFactory {

    @Override
    public Reservation makeReservation(Item item, User user) {
        if (!BoolValidator.notNull(item, user)) {
            return null;
        }
        if (item.getId() == 0 || user.getId() == 0) {
            return null;
        }

        return produceReservation(item.getId(), user.getId());
    }

    @Override
    public Reservation makeReservation(int itemId, int userId) {
        return produceReservation(itemId, userId);
    }

    private Reservation produceReservation(int itemId, int userId) {
        return new Reservation(new DataObjectFiller("reservation").add("user_id", userId).add("item_id", itemId).build());
    }
}
