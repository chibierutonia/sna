package databases;

import objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Ćwiertnia on 2017-08-05.
 */
public class UserDatabase {

    private static UserDatabase instance = null;
    protected UserDatabase() {
        userDatabase = new ArrayList<>();
        currentUser = new User();
    }
    public static UserDatabase getInstance() {
        if(instance == null) {
            instance = new UserDatabase();
        }
        return instance;
    }

    List<User> userDatabase;
    User currentUser;

    public List<User> getUserDatabase() {
        return userDatabase;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
