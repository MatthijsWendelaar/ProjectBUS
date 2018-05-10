package name.wendelaar.projectbus.manager;

import name.wendelaar.projectbus.database.models.User;

public interface IAuthenticationManager {

    public User authenticate(String email, String password);
}
