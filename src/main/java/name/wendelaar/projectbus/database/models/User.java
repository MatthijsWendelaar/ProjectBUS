package name.wendelaar.projectbus.database.models;

import name.wendelaar.projectbus.util.BooleanToStringConverter;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.model.Model;
import org.mindrot.jbcrypt.BCrypt;

public class User extends Model {

    public User(DataObject dataObject) {
        super(dataObject, "user");
    }

    public int getId() {
        return (int) dataObject.get("id");
    }

    public String getEmail() {
        return (String) dataObject.get("email");
    }

    public String getUserName() {
        return (String) dataObject.get("username");
    }

    public boolean isLibrarian() {
        return (boolean) dataObject.get("rank");
    }

    public String isLibrarianToString() {
        return BooleanToStringConverter.convert(dataObject.get("rank"));
    }

    public boolean hasSamePassword(String password) {
        Object object = dataObject.get("password");
        if (object == null) {
            return false;
        }

        return BCrypt.checkpw(password, object.toString());
    }

    public void setAccountDisabled(boolean disabled) {
        dataObject.set("disabled", disabled);
    }

    public boolean isAccountDisabled() {
        return (boolean) dataObject.get("disabled");
    }

    public String isAccountDisabledToString() {
        return BooleanToStringConverter.convert(dataObject.get("disabled"));
    }
}
