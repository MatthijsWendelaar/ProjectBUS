package name.wendelaar.projectbus.manager;

import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.database.models.UserData;

import java.util.Collection;

public interface IUserManager {

    public void createUser(User user);

    public void deleteUser(User user);

    public void disableUser(User user);

    public User getUser(int id);

    public UserData getUserData(int id);

    public User getUser(String username);

    public Collection<User> getUsers();
}
