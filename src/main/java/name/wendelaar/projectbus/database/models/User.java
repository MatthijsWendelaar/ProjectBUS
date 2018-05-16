package name.wendelaar.projectbus.database.models;

import name.wendelaar.projectbus.LlsApi;
import name.wendelaar.snowdb.annotations.Column;
import name.wendelaar.snowdb.annotations.Foreign;
import name.wendelaar.snowdb.annotations.Primary;
import name.wendelaar.snowdb.annotations.Table;
import name.wendelaar.snowdb.model.Model;

@Table(name = "user")
public class User extends Model {

    @Primary(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private boolean role;

    @Foreign(name = "added_by", foreignModel = User.class)
    private User addedBy;

    private UserData userData;
    private boolean requestedUserData = false;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLiberian() {
        return role;
    }

    public UserData getUserData() {
        if (userData == null && !requestedUserData) {
            userData = LlsApi.getUserManager().getUserData(id);
            requestedUserData = true;
        }
        return userData;
    }

    public boolean hasSamePassword(String password) {
        //TODO: add hashing!
        return this.password.equals(password);
    }
}
