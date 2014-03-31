package aau.sw8.recipe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import aau.sw8.model.Recipe;

/**
 * Created by jacob on 3/26/14.

 */
public class FavouriteList extends RecipeList {
    private boolean isLongClick = false; /*Enable OnCLick*/

    /*Constructors*/
    public FavouriteList(Context context) {
        super(context);
    }

    public FavouriteList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick(View view) {
        if (isLongClick != true) { /*if OnClick is enabled*/
            // Flash the image
            flashView(view);

            // The tag is the recipe which was clicked
            Recipe recipe = (Recipe) view.getTag();

            // Open recipe fragment
            ((MainActivity) this.getContext()).openRecipeFragment(recipe);
        }
    }

    @Override
    protected void onLongClick(final View view) {
        isLongClick = true; /*Disable OnClick functionality*/

        /* Alert dialog */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        /* Build Alert dialog*/
        builder.setMessage(R.string.remove_favourite) /*Set text description*/
                .setPositiveButton(R.string.button_remove, new DialogInterface.OnClickListener() {      /*Remove button*/
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO Remove Toast when not debugging anymore
                        Toast.makeText(getContext(), R.string.button_remove, Toast.LENGTH_SHORT).show(); /*Debug*/
                        isLongClick = false;    /*Enable the use of OnClick again*/
                        clearHighlight(view);   /*Clear the highlighting*/
                        //TODO Remove favourite from the list And remove it from favourites.
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {      /*Cancel button*/
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO Remove Toast when not debugging anymore
                        Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        isLongClick = false;    /*Enable the use of OnClick again*/
                        clearHighlight(view);   /*Clear the highlighting*/
                        /*Do nothing*/
                    }
                });

        builder.create();
        builder.show(); /*Show the Alert dialog*/
    }
}
