package aau.sw8.model;

import java.util.List;

/**
 * Created by jacob on 3/27/14.
 */
public class ShoppingList {
    private int shoppingId;
    private User user;
    private List<IngredientQuantity> ingredientList;

    public ShoppingList(int shoppingId, User user, List<IngredientQuantity> ingredientList) {
        this.shoppingId = shoppingId;
        this.user = user;
        this.ingredientList = ingredientList;
    }

    public int getShoppingId() {
        return shoppingId;
    }

    public User getUser() {
        return user;
    }

    public List<IngredientQuantity> getIngredientList() {
        return ingredientList;
    }
}
