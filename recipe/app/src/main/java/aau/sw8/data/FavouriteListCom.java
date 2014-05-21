package aau.sw8.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import aau.sw8.model.IntermediateRecipe;

/**
 * Created by jacob on 4/30/14.
 */
public class FavouriteListCom extends ServerComTask<ArrayList<IntermediateRecipe>> {
    public static final String GET = "get";
    private final String TAG = "FavouriteListCom";

    public FavouriteListCom(ServerAlertDialog serverAlertDialog, OnResponseListener<ArrayList<IntermediateRecipe>> onResponseListener, String action, String hash, int limit, int offset, String lang) {
        super(getPath(action, hash, limit, offset, lang), serverAlertDialog, onResponseListener);

    }

    private static String getPath(String action, String hash, int limit, int offset, String lang){
        String apiPath = "favourites.php";

        apiPath += "?action=" + action;
        apiPath += "&hash=" + hash;
        apiPath += "&limit=" + String.valueOf(limit);
        apiPath += "&offset=" + String.valueOf(offset);
        apiPath += "&lang=" +  lang;

        return apiPath;
    }

    @Override
    protected ArrayList<IntermediateRecipe> parseJson(String json) throws Exception {
        JSONArray favouritesArray = new JSONArray(json);

        ArrayList<IntermediateRecipe> intermediateRecipes = new ArrayList<>();

        for (int i = 0; i < favouritesArray.length(); i++){
            JSONObject favouriteObject = favouritesArray.getJSONObject(i);

            long id = favouriteObject.getLong("id");
            String name = favouriteObject.getString("name");
            //String description = favouriteObject.getString("description");
            String image = ServerComTask.getImagePath(favouriteObject.getString("image"));
            String missingIngredients = "";

            intermediateRecipes.add(new IntermediateRecipe(id, name, image, missingIngredients));
        }

        return intermediateRecipes;
    }
}
