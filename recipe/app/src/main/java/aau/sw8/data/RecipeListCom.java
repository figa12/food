package aau.sw8.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import aau.sw8.model.IntermediateRecipe;

/**
 * Created by Jesper on 0028 28. apr.
 */
public class RecipeListCom extends ServerComTask<ArrayList<IntermediateRecipe>> {

    public RecipeListCom(ServerAlertDialog serverAlertDialog, OnResponseListener<ArrayList<IntermediateRecipe>> onResponseListener, ArrayList<Long> chosenIngredients) {
        super(RecipeListCom.getPath(chosenIngredients), serverAlertDialog, onResponseListener);
    }

    private static String getPath(ArrayList<Long> chosenIngredients) {
        return "ingredientsearch.php?i=" + chosenIngredients.toString().replace(" ", "");
    }

    @Override
    protected ArrayList<IntermediateRecipe> parseJson(String json) throws Exception {
        JSONArray recipeArray = new JSONArray(json);

        ArrayList<IntermediateRecipe> intermediateRecipes = new ArrayList<>();

        for (int i = 0; i < recipeArray.length(); i++) {
            JSONObject recipeObject = recipeArray.getJSONObject(i);

            long id = recipeObject.getLong("id");
            String name = recipeObject.getString("name");
            String description = recipeObject.getString("description");
            String image = ServerComTask.getImagePath(recipeObject.getString("image"));
            ArrayList<Long> missingIngredients = new ArrayList<>();

            intermediateRecipes.add(new IntermediateRecipe(id, name, description, image, missingIngredients));
        }

        return intermediateRecipes;
    }
}
