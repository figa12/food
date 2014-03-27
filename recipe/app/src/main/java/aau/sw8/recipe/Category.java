package aau.sw8.recipe;

/**
 * Created by jacob on 3/27/14.
 */
public class Category {
    private int categoryId;
    private String name;
    private Category parent;
    private String imagepath;

    public Category(int categoryId, String name, Category parent, String imagepath) {
        this.categoryId = categoryId;
        this.name = name;
        this.parent = parent;
        this.imagepath = imagepath;
    }

    public int getId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    public String getImagepath() {
        return imagepath;
    }
}
