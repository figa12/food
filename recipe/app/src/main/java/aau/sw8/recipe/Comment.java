package aau.sw8.recipe;

/**
 * Created by jacob on 3/27/14.
 */
public class Comment {
    private int commentId;
    private User user;
    private Recipe recipe;
    private String text;

    public Comment(int commentId, User user, Recipe recipe, String text) {
        this.commentId = commentId;
        this.user = user;
        this.recipe = recipe;
        this.text = text;
    }

    public int getId() {
        return commentId;
    }

    public User getUser() {
        return user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getText() {
        return text;
    }
}
