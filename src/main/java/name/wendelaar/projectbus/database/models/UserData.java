package name.wendelaar.projectbus.database.models;

import name.wendelaar.simplevalidator.MatchValidator;
import name.wendelaar.snowdb.data.DataObject;
import name.wendelaar.snowdb.data.model.Model;

public class UserData extends Model {

    private User user;

    public UserData(DataObject dataObject, User user) {
        super(dataObject, "user_data_personal");
        this.user = user;
        match();
    }

    private void match() {
        if (user == null) {
            return;
        }
        Object o = dataObject.get("user_id");
        if (!MatchValidator.match(o, user.getId())) {
            throw new IllegalArgumentException("User does not match with UserData");
        }
    }

    public void printAll() {
        for (Object o : dataObject.getAll()) {
            System.out.println(o);
        }
    }
}
