package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.models.UserData;
import name.wendelaar.snowdb.data.DataObject;

import java.util.Collection;

public interface IUserManager {

    public void createUser(DataObject userObject, DataObject userDataObject);

    public void deleteUser(User user);

    public void disableUser(User user);

    public User getUser(int id);

    public UserData getUserData(int id);

    public User getUser(String username);

    public Collection<User> getUsersExceptOne(User user);

    public Collection<User> getUsers();
}
