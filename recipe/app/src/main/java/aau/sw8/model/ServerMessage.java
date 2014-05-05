package aau.sw8.model;

/**
 * Created by jacob on 4/29/14.
 */
public class ServerMessage {
    private boolean status;
    public static final boolean ERROR = false;
    public static final boolean SUCCESS = true;

    public ServerMessage(boolean status){
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
