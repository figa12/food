package aau.sw8.model;

/**
 * Created by jacob on 4/29/14.
 */
public class ServerMessage {
    private int status;
    private String description;
    public static final int ERROR = 0;
    public static final int SUCCES = 1;

    public ServerMessage(int status, String description){
        this.status = status;
        this.description = description;
    }


}
