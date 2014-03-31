package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class Ingredient {
    private int ingredientId;
    private String singlular;
    private String plural;
    private Category category;

    public Ingredient(int ingredientId, String singlular, String plural, Category category) {
        this.ingredientId = ingredientId;
        this.singlular = singlular;
        this.plural = plural;
        this.category = category;
    }

    public int getId(){
        return ingredientId;
    }

    public String getSinglular() {
        return singlular;
    }

    public String getPlural() {
        return plural;
    }

    public Category getCategory() {
        return category;
    }
}
