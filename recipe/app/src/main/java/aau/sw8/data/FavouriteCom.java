package aau.sw8.data;

import org.json.JSONObject;

import aau.sw8.model.ServerMessage;
import android.util.Log;

/**
 * Created by jacob on 4/29/14.
 */
public class FavouriteCom extends ServerComTask<ServerMessage> {
    private String action;
    private long recipeId;
    private String hash;
    public static final String ADD = "add";
    public static final String REMOVE = "remove";
    public static final String STATUS = "status";
    private final String TAG = "FavouriteCom";

    public FavouriteCom(ServerAlertDialog serverAlertDialog, OnResponseListener<ServerMessage> onResponseListener,
                        String action, long recipeId, String hash){
        super(getPath(action, recipeId, hash), serverAlertDialog, onResponseListener);
        Log.w(TAG, "server query: " + getPath(action, recipeId, hash));
        this.action = action;
        this.recipeId = recipeId;
        this.hash = hash;
    }

    public static String getPath(String action, long recipeId, String hash){
        String apiPath = "favourites.php?action=";

        if (action.equals(ADD) || action.equals(REMOVE) || action.equals(STATUS)){
            apiPath = apiPath + action + "&recipeId=" + recipeId + "&hash=" + hash;
        } else{
            apiPath = apiPath + "unknown";
        }

        return apiPath;
    }

    @Override
    protected ServerMessage parseJson(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);

        int status = jsonObject.getInt("status");
        String description = jsonObject.getString("description");

        Log.w(TAG, "ServerMessage Json Parsed: " + String.valueOf(status) + ", " + description);
        return new ServerMessage(status, description);
    }
}
