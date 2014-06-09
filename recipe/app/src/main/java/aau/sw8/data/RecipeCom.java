package aau.sw8.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aau.sw8.model.ExchangeableIngredient;
import aau.sw8.model.Ingredient;
import aau.sw8.model.IngredientGroup;
import aau.sw8.model.IngredientQuantity;
import aau.sw8.model.InstructionStep;
import aau.sw8.model.Recipe;
import aau.sw8.model.Unit;

/**
 * Created by Jesper apr.
 */
public class RecipeCom extends ServerComTask<Recipe> {

    private long recipeId;

    public RecipeCom(ServerAlertDialog serverAlertDialog, OnResponseListener<Recipe> onResponseListener, long recipeId) {
        super(RecipeCom.getPath(recipeId), serverAlertDialog, onResponseListener);
        this.recipeId = recipeId;
    }

    private static String getPath(long recipeId) {
        return "recipe.php?id=" + recipeId + "&lang=us&metric=metric";
    }

    @Override
    protected Recipe parseJson(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);

        String name = jsonObject.getString("name");
        String description = jsonObject.getString("desc");
        String imagePath = ServerComTask.getImagePath(jsonObject.getString("image"));

        long upvotes = jsonObject.getLong("upvotes");
        long downvotes = jsonObject.getLong("downvotes");

        ArrayList<IngredientGroup> ingredientGroups = new ArrayList<>();
        ArrayList<InstructionStep> instructionSteps = new ArrayList<>();

        JSONArray groupsArray = jsonObject.getJSONArray("groups");

        for (int i = 0; i < groupsArray.length(); i++) {
            ingredientGroups.add(this.parseIngredientGroup(groupsArray.getJSONObject(i)));
        }

        if (!jsonObject.isNull("steps")) {
            JSONArray stepsArray = jsonObject.getJSONArray("steps");

            for (int i = 0; i < stepsArray.length(); i++) {
                instructionSteps.add(RecipeCom.parseInstructionStep(stepsArray.getJSONObject(i), i + 1));
            }
        }

        String source = jsonObject.getString("source");
        String licenseName = jsonObject.getString("licensename");
        String licenseLink = jsonObject.getString("licenselink");
        String licenseNote = jsonObject.getString("licensenote");

        return new Recipe(this.recipeId, imagePath, name, description, ingredientGroups, instructionSteps, upvotes, downvotes, source, licenseName, licenseLink, licenseNote);
    }

    private IngredientGroup parseIngredientGroup(JSONObject groupObject) throws JSONException{
        String groupName = groupObject.getString("name");
        //int order = groupObject.getInt("order");

        ArrayList<ExchangeableIngredient> exchangeableIngredients = new ArrayList<>();
        JSONArray exchangesArray = groupObject.getJSONArray("exchanges");

        for (int i = 0; i < exchangesArray.length(); i++) {
            exchangeableIngredients.add(RecipeCom.parseExchangeableIngredient(exchangesArray.getJSONObject(i)));
        }

        return new IngredientGroup(0L, groupName, exchangeableIngredients);
    }

    private static ExchangeableIngredient parseExchangeableIngredient(JSONObject exchangeableObject) throws JSONException {
        boolean mandatory = exchangeableObject.getBoolean("mandatory");
        //int order = exchangeableObject.getInt("order");
        ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
        JSONArray ingredientQuantitiesArray = exchangeableObject.getJSONArray("ingredients");

        for (int i = 0; i < ingredientQuantitiesArray.length(); i++) {
            ingredientQuantities.add(RecipeCom.parseIngredientQuantity(ingredientQuantitiesArray.getJSONObject(i)));
        }

        return new ExchangeableIngredient(0L, ingredientQuantities, mandatory);
    }

    private static IngredientQuantity parseIngredientQuantity(JSONObject quantityObject) throws JSONException {
        String ingredientName = quantityObject.getString("name");
        double amount = quantityObject.getDouble("amount");
        Unit unit = null;

        if (!quantityObject.isNull("unit")) {
            String unitName = quantityObject.getString("unit");
            unit = new Unit(0L, unitName, unitName, 0f); //TODO conversion missing
        }

        return new IngredientQuantity(0L, new Ingredient(0L, ingredientName, ingredientName, null), unit, amount);
    }

    private static InstructionStep parseInstructionStep(JSONObject instructionObject, int stepNumber) throws JSONException {
        String description = instructionObject.getString("desc");
        String image = null;
        if (!instructionObject.isNull("image")) {
            image = ServerComTask.getImagePath(instructionObject.getString("image"));
        }
        //int order = instructionObject.getInt("order");
        return new InstructionStep(description, stepNumber, image);
    }
}
