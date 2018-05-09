package name.wendelaar.projectbus.database.models;

import name.wendelaar.matthijs.snowdb.annotations.Column;
import name.wendelaar.matthijs.snowdb.annotations.Foreign;
import name.wendelaar.matthijs.snowdb.annotations.Primary;
import name.wendelaar.matthijs.snowdb.annotations.Table;
import name.wendelaar.matthijs.snowdb.model.Model;

import java.sql.Date;

@Table(name = "item")
public class Item extends Model {

    @Primary(name = "id")
    private int id;

    @Column(name = "item_name")
    private String name;

    @Foreign(name = "item_type_id", foreignModel = ItemType.class)
    private ItemType type;

    @Column(name = "loaned_out")
    private boolean loanedOut;

    @Foreign(name = "user_id", foreignModel = User.class)
    private User borrower;

    @Column(name = "loaned_out_at")
    private Date loanedOutAt;

    @Column(name = "to_late")
    private boolean toLate;

    @Column(name = "loaned_out_count")
    private int loandedOutCount;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isLoanedOut() {
        return loanedOut;
    }

    public ItemType getType() {
        return type;
    }

    public User getBorrower() {
        return borrower;
    }

    public boolean isBrorrowerToLate() {
        return toLate;
    }

    public int getLoandedOutCount() {
        return loandedOutCount;
    }
}
