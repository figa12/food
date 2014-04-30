package aau.sw8.recipe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;

import aau.sw8.model.IntermediateRecipe;

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
        if (!isLongClick) { /*if OnClick is enabled*/
            super.onClick(view);
        }
    }

    @Override
    protected void onLongClick(final IntermediateRecipe recipe, final View view) {
        isLongClick = true; /*Disable OnClick functionality*/

        /* Alert dialog */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        /* Build Alert dialog*/
        builder.setMessage(R.string.remove_favourite) /*Set text description*/
                .setPositiveButton(R.string.button_remove, new DialogInterface.OnClickListener() {      /*Remove button*/
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isLongClick = false;    /*Enable the use of OnClick again*/
                        clearHighlight(view);   /*Clear the highlighting*/
                        removeView(recipe);     /*remove the recipe from the phone's favourite list*/
                        //TODO Remove the favourite from the database
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {      /*Cancel button*/
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isLongClick = false;    /*Enable the use of OnClick again*/
                        clearHighlight(view);   /*Clear the highlighting*/
                        /*Do nothing*/
                    }
                });

        builder.create();
        builder.show(); /*Show the Alert dialog*/
    }


}
