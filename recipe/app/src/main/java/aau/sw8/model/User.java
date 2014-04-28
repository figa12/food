package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class User {
    private String hash;
    private String token;
    private String personName;

    public User(String personName, String hash, String token){
        this.personName = personName;
        this.hash = hash;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public static String doHash(String username){
        String hash = "";
        return hash;
    }
}
