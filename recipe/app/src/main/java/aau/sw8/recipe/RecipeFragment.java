package aau.sw8.recipe;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aau.sw8.model.Comment;
import aau.sw8.model.ExchangeableIngredient;
import aau.sw8.model.IngredientGroup;
import aau.sw8.model.InstructionStep;
import aau.sw8.model.Recipe;
import aau.sw8.model.User;

/**
 * Created by Jesper on 12-03-14.
 */
public class RecipeFragment extends Fragment {

    private static final float FONT_SIZES[] = { 12f, 14f, 19f, 24f };

    public static final String ARG_RECIPE = "recipe";

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    private Recipe recipe;

    private String pageTitle;

    private InstructionList instructionList;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);
        this.recipe = super.getArguments().getParcelable(RecipeFragment.ARG_RECIPE);

        // TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = super.getResources().getString(R.string.fragment_recipe);
        super.getActivity().setTitle(this.pageTitle);

        ImageView recipeImage = (ImageView) rootView.findViewById(R.id.recipeHeaderImageView);
        this.imageLoader.displayImage(this.recipe.getImagePath(), recipeImage, this.imageLoaderOptions);

        TextView recipeName = (TextView) rootView.findViewById(R.id.recipeName);
        recipeName.setText(this.recipe.getRecipeTitle());

        TextView description = (TextView) rootView.findViewById(R.id.recipeDescription);
        description.setText(this.recipe.getRecipeDescription());

        // create and add the ingredient groups
        LinearLayout ingredientGroupsLayout = (LinearLayout) rootView.findViewById(R.id.ingredientGroupsLinearLayout);
        for (IngredientGroup ingredientGroup : this.recipe.getIngredientGroups()) {
            // create a header for each group
            TextView ingredientGroupHeader = new TextView(this.getActivity());
            ingredientGroupHeader.setText(ingredientGroup.getName());
            ingredientGroupHeader.setTypeface(null, Typeface.BOLD_ITALIC);
            ingredientGroupHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            ingredientGroupsLayout.addView(ingredientGroupHeader);

            // create the ingredient list
            RecipeIngredientGroupList ingredientGroupList = new RecipeIngredientGroupList(this.getActivity());

            // Set vertical margin on each ingredient group list
            // get the dp vertical dimen and convert it to pixels
            Resources resources = this.getActivity().getResources();
            int pxMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16f, //resources.getDimension(R.dimen.activity_vertical_margin),
                    resources.getDisplayMetrics()
            );

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, pxMargin);
            ingredientGroupList.setLayoutParams(params);

            // add the exchangeable ingredients to the group
            for (ExchangeableIngredient exchangeableIngredient : ingredientGroup.getExchangeableIngredients()) {
                ingredientGroupList.addView(exchangeableIngredient);
            }

            // add the ingredient group list
            ingredientGroupsLayout.addView(ingredientGroupList);
        }

        this.instructionList = (InstructionList) rootView.findViewById(R.id.instructionList);
        for (InstructionStep instructionStep : this.recipe.getInstructionSteps()) {
            this.instructionList.addView(instructionStep);
        }

        Spinner fontSpinner = (Spinner) rootView.findViewById(R.id.fontSpinner);
        fontSpinner.setSelection(1); // second element is default size
        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selected = RecipeFragment.this.getResources().getStringArray(R.array.font_sizes)[position];
                float size = RecipeFragment.FONT_SIZES[position];
                RecipeFragment.this.instructionList.setFontSize(size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // CAN THIS HAPPEN?!?!
                // I DON'T FUCKING KNOW?!?!
            }
        });

        RecipeCommentList commentList = (RecipeCommentList) rootView.findViewById(R.id.recipeCommentList);
        for (Comment comment : this.recipe.getComments()) {
            commentList.addView(comment);
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handles presses on the action bar items
        switch (item.getItemId()){
            case R.id.favourite_button:
                addToFavourite(this.recipe, ((MainActivity)this.getActivity()).getUser());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addToFavourite(Recipe recipe, User user){
        if(user != null){
            //add the recipe to the user's favourites
        }else {
            /* Alert dialog */
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

            /* Build Alert dialog*/
            builder.setMessage(R.string.sign_in) /*Set text description*/
                    .setPositiveButton(R.string.sign_in_OK, new DialogInterface.OnClickListener() {         /*Sign in button*/
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //TODO add recipe to favourites
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {      /*Cancel button*/
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            /*Do nothing*/
                        }
                    });

            builder.create();
            builder.show(); /*Show the Alert dialog*/
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu

        //Show the favourite button, is depandant on "isDrawerOpen()"
        menu.findItem(R.id.favourite_button).setVisible(!mainActivity.isDrawerOpen());

        super.onPrepareOptionsMenu(menu);
    }
}
