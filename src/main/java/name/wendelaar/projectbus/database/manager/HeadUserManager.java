package name.wendelaar.projectbus.database.manager;

import name.wendelaar.matthijs.snowdb.manager.Manager;
import name.wendelaar.projectbus.database.models.User;
import name.wendelaar.projectbus.manager.IAuthenticationManager;
import name.wendelaar.projectbus.manager.IUserManager;
import name.wendelaar.projectbus.view.MainManager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class HeadUserManager implements IUserManager, IAuthenticationManager {

    private MainManager mainManager;
    private User currentUser;

    public HeadUserManager(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void disableUser(User user) {

    }

    @Override
    public User getUser(int id) {
        User user = null;
        try {
            user = Manager.create().prepare("SELECT * FROM user WHERE id = ? LIMIT 1", id).findOne(User.class);
        } catch (SQLException ex) {
            ex.printStackTrace(); //TODO: Good error handling!
        }
        return user;
    }

    @Override
    public User getUser(String username) {
        User user = null;
        try {
            user = Manager.create().prepare("SELECT * FROM user WHERE username = ? LIMIT 1", username).findOne(User.class);
        } catch (SQLException ex) {
            ex.printStackTrace(); //TODO: Good error handling!
        }
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        List<User> user = null;
        try {
            user = Manager.create().prepare("SELECT * FROM user").find(User.class);
        } catch (SQLException ex) {
            ex.printStackTrace(); //TODO: Good error handling!
        }
        return user;
    }

    @Override
    public boolean authenticate(String email, String password) {
        User user = null;
        //TODO:
        try {
            user = Manager.create().prepare("SELECT * FROM user WHERE email = ? LIMIT 1", email).findOne(User.class);
        } catch (SQLException ex) {
            ex.printStackTrace(); //TODO: Good error handling!
        }
        if (user == null || !user.hasSamePassword(password)) {
            return false;
        }
        currentUser = user;

        return true;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}
