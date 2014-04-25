package aau.sw8.data;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import aau.sw8.model.Category;
import aau.sw8.model.Ingredient;

/**
 * Created by Jesper on 0023 23. apr.
 */
public class IngredientCom extends ServerComTask<ArrayList<Ingredient>> {

    public IngredientCom(ServerAlertDialog serverAlertDialog, OnResponseListener<ArrayList<Ingredient>> onResponseListener) {
        super("ingredients.php?lang=us", serverAlertDialog, onResponseListener);
    }

    @Override
    protected ArrayList<Ingredient> parseJson(String json) throws Exception {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            long id = jsonObject.getLong("id");
            String name = jsonObject.getString("name");
            Category category = null;

            if (jsonObject.has("category")) {
                //TODO parse category
            }

            ingredients.add(new Ingredient(id, name, name, category));
        }

        return ingredients;
    }
}
