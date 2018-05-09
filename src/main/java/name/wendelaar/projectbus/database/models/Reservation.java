package name.wendelaar.projectbus.database.models;

import name.wendelaar.matthijs.snowdb.annotations.Column;
import name.wendelaar.matthijs.snowdb.annotations.Foreign;
import name.wendelaar.matthijs.snowdb.annotations.Primary;
import name.wendelaar.matthijs.snowdb.model.Model;

import java.sql.Timestamp;

public class Reservation extends Model {

    @Primary(name = "id")
    private int id;

    @Foreign(name = "user_id", foreignModel = User.class)
    private User reservationHolder;

    @Foreign(name = "item_id", foreignModel = Item.class)
    private Item reservedItem;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public int getId() {
        return id;
    }

    public User getReservationHolder() {
        return reservationHolder;
    }

    public Item getReservedItem() {
        return reservedItem;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
