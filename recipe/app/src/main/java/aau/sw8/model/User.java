package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class User {
    private int userId;
    private String token;
    private String username;

    public User(int userId, String token, String username) {
        this.userId = userId;
        this.token = token;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
