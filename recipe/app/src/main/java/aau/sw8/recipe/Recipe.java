package aau.sw8.recipe;

/**
 * Created by jeria_000 on 07-03-14.
 */
public class Recipe {

    private String imagePath;
    private String recipeTitle;
    private String recipeDescription;

    public Recipe(String imagePath, String recipeTitle, String recipeDescription) {
        this.imagePath = imagePath;
        this.recipeTitle = recipeTitle;
        this.recipeDescription = recipeDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }
}
