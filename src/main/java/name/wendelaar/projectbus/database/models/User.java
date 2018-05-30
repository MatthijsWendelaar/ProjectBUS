package name.wendelaar.projectbus.database.models;

import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.model.Model;

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

    public boolean isLiberian() {
        return (boolean) dataObject.get("rank");
    }

    public boolean hasSamePassword(String password) {
        //TODO: add hashing!
        Object object = dataObject.get("password");
        if (object == null) {
            return false;
        }

        return object.equals(password);
    }

    public void printAll() {
        for (Object o : dataObject.getAll()) {
            System.out.println(o);
        }
    }
}
