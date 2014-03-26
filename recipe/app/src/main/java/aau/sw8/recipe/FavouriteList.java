package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jacob on 3/26/14.

 */
public class FavouriteList extends RecipeList {

    public FavouriteList(Context context) {
        super(context);
    }

    public FavouriteList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLongClick(View view) {
        // TODO long click
    }
}
