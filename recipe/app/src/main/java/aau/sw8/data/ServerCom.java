package aau.sw8.data;

import android.content.Context;

/**
 * Created by jacob on 3/31/14.
 */

public class ServerCom {

    /*Variables*/
    private static ServerCom instance = null;

    private Context context;

    /*Constructors*/
    private ServerCom() { }

    public static ServerCom getInstance(){
        if(instance == null){
            instance = new ServerCom();
        }
        return instance;
    }

    /*Class methods*/
    public void init(Context context) {
        this.context = context.getApplicationContext();
    }
}


