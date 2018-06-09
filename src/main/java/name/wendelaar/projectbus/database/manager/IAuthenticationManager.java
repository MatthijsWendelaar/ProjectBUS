package name.wendelaar.projectbus.database.manager;

import name.wendelaar.projectbus.database.models.User;

public interface IAuthenticationManager {

    public boolean authenticate(String email, String password);

    public void logout();

    public User getCurrentUser();
}
