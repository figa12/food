package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class User {
    private long userId;
    private String token;
    private String username;

    /***
     *
     * @param userId
     * @param username
     * @param token
     */
    public User(long userId, String username, String token) {
        this.userId = userId;
        this.token = token;
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
