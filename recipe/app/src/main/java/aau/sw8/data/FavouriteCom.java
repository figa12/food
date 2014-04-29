package aau.sw8.data;

import org.json.JSONObject;

import aau.sw8.model.ServerMessage;
import aau.sw8.recipe.FavouriteList;

/**
 * Created by jacob on 4/29/14.
 */
public class FavouriteCom extends ServerComTask<ServerMessage> {
    private String action;
    private long recipeId;
    private String hash;

    public FavouriteCom(ServerAlertDialog serverAlertDialog, OnResponseListener<ServerMessage> onResponseListener,
                        String action, long recipeId, String hash){
        super(getPath(action, recipeId, hash), serverAlertDialog, onResponseListener);

        this.action = action;
        this.recipeId = recipeId;
        this.hash = hash;
    }

    public static String getPath(String action, long recipeId, String hash){
        String apiPath = "favourites.php?action=";

        if (action.equals("add") || action.equals("remove")){
            apiPath = apiPath + action + "?recipeId=" + recipeId + "?hash=" + hash;
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

        ServerMessage serverMessage = new ServerMessage(status, description);

        return serverMessage;
    }
}
