package name.wendelaar.database.models;

import name.wendelaar.matthijs.snowdb.annotations.Column;
import name.wendelaar.matthijs.snowdb.annotations.Foreign;
import name.wendelaar.matthijs.snowdb.annotations.Primary;
import name.wendelaar.matthijs.snowdb.annotations.Table;
import name.wendelaar.matthijs.snowdb.model.Model;

@Table(name = "item")
public class Item extends Model {

    @Primary(name = "id")
    private int id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "loaned_out")
    private boolean loanedOut;

    @Foreign(name = "item_type_id", foreignModel = ItemType.class)
    private ItemType type;

    @Foreign(name = "user_id", foreignModel = User.class)
    private User borrower;

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
}
