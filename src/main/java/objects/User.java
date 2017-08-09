package objects;

import java.util.Date;
import java.util.List;

/**
 * Created by Robert Ćwiertnia on 2017-08-05.
 */
public class User {

    private String userId;
    private String userName;
    private Date registerDate;
    private List<String> followeesNamesList;

    public User() {
    }

    public User(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.registerDate = user.getRegisterDate();
        this.followeesNamesList = user.getFolloweesNamesList();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public List<String> getFolloweesNamesList() {
        return followeesNamesList;
    }

    public void setFolloweesNamesList(List<String> followeesNamesList) {
        this.followeesNamesList = followeesNamesList;
    }
}
