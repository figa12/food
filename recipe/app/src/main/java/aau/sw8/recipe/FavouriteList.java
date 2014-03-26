package aau.sw8.recipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by jacob on 3/26/14.

 */
public class FavouriteList extends RecipeList {
    private boolean isLongClick = false;

    /*Constructors*/
    public FavouriteList(Context context) {
        super(context);
    }

    public FavouriteList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick(View view) {
        // Flash the image
        flashView(view);

        if (isLongClick != true) {
            // The tag is the recipe which was clicked
            Recipe recipe = (Recipe) view.getTag();

            // Open recipe fragment
            ((MainActivity) this.getContext()).openRecipeFragment(recipe);
        }
    }

    @Override
    protected void onLongClick(final View view) {
        isLongClick = true;
        /*implement remove from favourite and set isLongClick = false*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(R.string.remove_favourite)
                .setPositiveButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                        isLongClick = false;
                        clearHighlight(view);

                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        isLongClick = false;
                        clearHighlight(view);

                    }
                });

        builder.create();
        builder.show();
    }
}
