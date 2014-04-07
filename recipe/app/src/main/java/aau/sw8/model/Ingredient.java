package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class Ingredient {
    private int ingredientId;
    private String singular;
    private String plural;
    private Category category;

    public Ingredient(int ingredientId, String singular, String plural, Category category) {
        this.ingredientId = ingredientId;
        this.singular = singular;
        this.plural = plural;
        this.category = category;
    }

    public int getId(){
        return ingredientId;
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }

    public Category getCategory() {
        return category;
    }
}
