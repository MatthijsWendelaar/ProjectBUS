package name.wendelaar.projectbus.database.models.factory;

import name.wendelaar.projectbus.database.models.Item;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.simplevalidator.BoolValidator;
import name.wendelaar.snowdb.data.DataObject;

public class ReservationFactory implements IReservationFactory {

    @Override
    public DataObject makeReservation(Item item, User user) {
        if (!BoolValidator.notNull(item, user)) {
            return null;
        }
        if (item.getId() == 0 || user.getId() == 0) {
            return null;
        }

        return produceReservation(item.getId(), user.getId());
    }

    @Override
    public DataObject makeReservation(int itemId, int userId) {
        return produceReservation(itemId, userId);
    }

    private DataObject produceReservation(int itemId, int userId) {
        return new DataObjectFiller("reservation").add("user_id", userId).add("item_id", itemId).build();
    }
}
