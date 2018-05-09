package name.wendelaar.projectbus.database.models;

import name.wendelaar.matthijs.snowdb.annotations.Column;
import name.wendelaar.matthijs.snowdb.annotations.Primary;
import name.wendelaar.matthijs.snowdb.annotations.Table;
import name.wendelaar.matthijs.snowdb.model.Model;

@Table(name = "user")
public class User extends Model {

    @Primary(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "rank")
    private int rank;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLiberian() {
        return rank == 1;
    }
}
