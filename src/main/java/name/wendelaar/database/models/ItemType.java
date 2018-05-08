package name.wendelaar.database.models;

import name.wendelaar.matthijs.snowdb.annotations.Column;
import name.wendelaar.matthijs.snowdb.annotations.Primary;
import name.wendelaar.matthijs.snowdb.annotations.Table;
import name.wendelaar.matthijs.snowdb.model.Model;

@Table(name = "item_type")
public class ItemType extends Model {

    @Primary(name = "id")
    private int id;

    @Column(name = "type_name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
