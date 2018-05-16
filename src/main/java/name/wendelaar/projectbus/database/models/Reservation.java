package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.annotations.Column;
import name.wendelaar.snowdb.annotations.Foreign;
import name.wendelaar.snowdb.annotations.Primary;
import name.wendelaar.snowdb.annotations.Table;
import name.wendelaar.snowdb.model.Model;

import java.sql.Date;

@Table(name = "reservation")
public class Reservation extends Model {

    @Primary(name = "id")
    private int id;

    @Foreign(name = "user_id", foreignModel = User.class)
    private User reservationHolder;

    @Foreign(name = "item_id", foreignModel = Item.class)
    private Item reservedItem;

    @Column(name = "created_at")
    private Date createdAt;

    public int getId() {
        return id;
    }

    public User getReservationHolder() {
        return reservationHolder;
    }

    public Item getReservedItem() {
        return reservedItem;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
