package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import aau.sw8.model.Recipe;

/**
 * Created by Sam on 26/03/2014.
 */
public class SearchList extends RecipeList {

    public SearchList(Context context) {
        super(context);
    }

    public SearchList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected View makeView(Recipe recipe) {
        View recipeView = super.makeView(recipe);

        // TODO proper data
        TextView notification = (TextView) recipeView.findViewById(R.id.notification);

        first = true;

        moreNotifications(notification);
        moreNotifications(notification);
        moreNotifications(notification);
        moreNotifications(notification);
        moreNotifications(notification);



        TextView rating = (TextView) recipeView.findViewById(R.id.rating);

        for (int i = random.nextInt(5); i != 5; ++i) {
            rating.append("★");
        }

        return recipeView;
    }

    private Random random = new Random();

    private boolean first = true;

    private void moreNotifications(TextView notificationView) {
        String[] strings = new String[]{"Apples", "Lemons", "Eggs", "Flour", "Sugar"};

        if (random.nextBoolean()) {
            if (first)
                first = false;
            else
                notificationView.append(", ");


            notificationView.append(strings[random.nextInt(5)]);
        }

    }

    @Override
    protected void onClick(View view) {
        // Flash the image
        flashView(view);

        // The tag is the recipe which was clicked
        Recipe recipe = (Recipe) view.getTag();

        // Open recipe fragment
        ((MainActivity) this.getContext()).openRecipeFragment(recipe);
    }
}
