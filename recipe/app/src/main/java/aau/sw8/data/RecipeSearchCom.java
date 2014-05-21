package aau.sw8.data;

import android.net.Uri;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import aau.sw8.model.IntermediateRecipe;

/**
 * Created by Jesper on 0001 1. maj.
 */
public class RecipeSearchCom extends ServerComTask<ArrayList<IntermediateRecipe>> {

    public RecipeSearchCom(ServerAlertDialog serverAlertDialog, OnResponseListener<ArrayList<IntermediateRecipe>> onResponseListener, String query, int offset, int limit, BasicNameValuePair... basicNameValuePairs) {
        super(RecipeSearchCom.getPath(query, offset, limit), serverAlertDialog, onResponseListener, basicNameValuePairs);
    }

    private static String getPath(String query, int offset, int limit) {
        return "textsearch.php?q=" + Uri.encode(query) + "&offset=" + offset + "&limit=" + limit;
    }

    @Override
    protected ArrayList<IntermediateRecipe> parseJson(String json) throws Exception {
        JSONArray recipesArray = new JSONArray(json);

        ArrayList<IntermediateRecipe> intermediateRecipes = new ArrayList<>();

        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject recipeObject = recipesArray.getJSONObject(i);

            long id = recipeObject.getLong("id");
            String name = recipeObject.getString("name");
            //String description = recipeObject.getString("description");
            String image = ServerComTask.getImagePath(recipeObject.getString("image"));

            intermediateRecipes.add(new IntermediateRecipe(id, name, image));
        }

        return intermediateRecipes;
    }
}
