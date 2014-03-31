package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class User {
    private int userId;
    private int token;
    private String username;

    public User(int userId, int token, String username) {
        this.userId = userId;
        this.token = token;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public int getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
