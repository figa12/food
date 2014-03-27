package aau.sw8.recipe;

import java.util.List;

/**
 * Created by jacob on 3/27/14.
 */
public class ShoppingList {
    private int shoppingId;
    private User user;
    private List<Quantity> ingredientList;

    public ShoppingList(int shoppingId, User user, List<Quantity> ingredientList) {
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

    public List<Quantity> getIngredientList() {
        return ingredientList;
    }
}
