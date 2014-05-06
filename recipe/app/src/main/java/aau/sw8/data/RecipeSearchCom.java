package aau.sw8.data;

import android.net.Uri;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import aau.sw8.model.IntermediateRecipe;

/**
 * Created by jeria_000 on 0001 1. maj.
 */
public class RecipeSearchCom extends ServerComTask<ArrayList<IntermediateRecipe>> {

    public RecipeSearchCom(ServerAlertDialog serverAlertDialog, OnResponseListener<ArrayList<IntermediateRecipe>> onResponseListener, String query, BasicNameValuePair... basicNameValuePairs) {
        super("textsearch.php?q=" + Uri.encode(query), serverAlertDialog, onResponseListener, basicNameValuePairs);
    }

    @Override
    protected ArrayList<IntermediateRecipe> parseJson(String json) throws Exception {
        JSONArray recipesArray = new JSONArray(json);

        ArrayList<IntermediateRecipe> intermediateRecipes = new ArrayList<>();

        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject recipeObject = recipesArray.getJSONObject(i);

            long id = recipeObject.getLong("id");
            String name = recipeObject.getString("name");
            String description = recipeObject.getString("description");
            String image = ServerComTask.getImagePath(recipeObject.getString("image"));

            intermediateRecipes.add(new IntermediateRecipe(id, name, description, image));
        }

        return intermediateRecipes;
    }
}
