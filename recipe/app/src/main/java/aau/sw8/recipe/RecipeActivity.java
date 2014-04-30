package aau.sw8.recipe;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aau.sw8.data.FavouriteCom;
import aau.sw8.data.RecipeCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.Comment;
import aau.sw8.model.ExchangeableIngredient;
import aau.sw8.model.IngredientGroup;
import aau.sw8.model.InstructionStep;
import aau.sw8.model.Recipe;
import aau.sw8.model.ServerMessage;
import aau.sw8.model.User;

/**
 * Created by Sam on 14/04/2014.
 */
public class RecipeActivity extends DrawerActivity implements ObservableScrollView.ScrollListener {

    private static final float FONT_SIZES[] = { 12f, 14f, 19f, 24f };

    public static final String ARG_RECIPE_ID = "recipeId";

    private final String TAG = "RecipeActivity";

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    private Recipe recipe;

    private String pageTitle;

    private InstructionList instructionList;

    private boolean isFavourited = false;

    private MenuItem favouriteButton;

    /*Override methods*/
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        this.setupActionBar();

        this.instructionList = (InstructionList) findViewById(R.id.instructionList);

        this.downloadRecipe(getIntent().getExtras().getLong(RecipeActivity.ARG_RECIPE_ID));

        // set up font size spinner
        Spinner fontSpinner = (Spinner) findViewById(R.id.fontSpinner);
        fontSpinner.setSelection(1); // second element is default size
        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                float size = RecipeActivity.FONT_SIZES[position];
                RecipeActivity.this.instructionList.setFontSize(size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // CAN THIS HAPPEN?!?!
                // I DON'T FUCKING KNOW?!?!
                throw new RuntimeException("wut??! onNothingSelected just happened");
            }
        });

        ObservableScrollView scroller = (ObservableScrollView) findViewById(R.id.recipe_scroller);
        scroller.setScrollViewListener(this);



    }

    private void downloadRecipe(long id) {
        /*if (id == 0L) {
            return;
        }*/

        new RecipeCom(this, new ServerComTask.OnResponseListener<Recipe>() {
            @Override
            public void onResponse(Recipe result) {
                RecipeActivity.this.recipe = result;
                RecipeActivity.this.insertRecipeData();
            }
        }, 1L); //TODO static id, change bitte

        if(LogInActivity.user != null) {
            new FavouriteCom(this, new ServerComTask.OnResponseListener<ServerMessage>() {
                @Override
                public void onResponse(ServerMessage result) {
                    if(result.getStatus() == ServerMessage.FAVOURITED){
                        isFavourited = true;

                    }else{
                        isFavourited = false;
                    }
                }
            }, FavouriteCom.STATUS, this.recipe.getRecipeId(), LogInActivity.user.getHash());
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupActionBar() {
        ActionBar actionBar = this.getActionBar();
        actionBar.setIcon(R.drawable.ic_transparent);
        this.setTitle("                                                          ");

        super.drawerToggle.setDrawerIndicatorEnabled(false);

        this.drawerTitle = "";
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) super.drawerLinearLayout.getLayoutParams();
        params.topMargin = this.getActionBarHeight();
        super.drawerLinearLayout.setLayoutParams(params);
    }

    @SuppressWarnings("ConstantConditions")
    private void insertRecipeData() {
        ImageView recipeImage = (ImageView) findViewById(R.id.recipe_picture);
        this.imageLoader.displayImage(this.recipe.getImagePath(), recipeImage, this.imageLoaderOptions);

        TextView recipeName = (TextView) findViewById(R.id.recipe_name);
        recipeName.setText(this.recipe.getRecipeTitle());

        TextView description = (TextView) findViewById(R.id.recipeDescription);
        description.setText(this.recipe.getRecipeDescription());

        // create and add the ingredient groups
        LinearLayout ingredientGroupsLayout = (LinearLayout) findViewById(R.id.ingredientGroupsLinearLayout);
        for (IngredientGroup ingredientGroup : this.recipe.getIngredientGroups()) {
            // create a header for each group
            TextView ingredientGroupHeader = new TextView(this);
            ingredientGroupHeader.setText(ingredientGroup.getName());
            ingredientGroupHeader.setTypeface(null, Typeface.BOLD_ITALIC);
            ingredientGroupHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            ingredientGroupsLayout.addView(ingredientGroupHeader);

            // create the ingredient list
            RecipeIngredientGroupList ingredientGroupList = new RecipeIngredientGroupList(this);

            // Set vertical margin on each ingredient group list
            // get the dp vertical dimen and convert it to pixels
            Resources resources = this.getResources();
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

        for (InstructionStep instructionStep : this.recipe.getInstructionSteps()) {
            this.instructionList.addView(instructionStep);
        }

        RecipeCommentList commentList = (RecipeCommentList) findViewById(R.id.recipeCommentList);
        for (Comment comment : this.recipe.getComments()) {
            commentList.addView(comment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.getMenuInflater().inflate(R.menu.recipe, menu);

        favouriteButton = menu.findItem(R.id.favourite_button);

        if(isFavourited) {
            favouriteButton.setIcon(android.R.drawable.btn_star_big_on);
        }else{
            favouriteButton.setIcon(android.R.drawable.btn_star_big_off);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (super.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (this.isDrawerOpen()) {
            super.drawerLayout.closeDrawers();
            return true;
        }

        // Handle action buttons
        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.favourite_button:
                if (LogInActivity.user != null){
                     addOrRemoveFromFavourite(!isFavourited, this.recipe, LogInActivity.user);
                }else{
                    showLoginDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addOrRemoveFromFavourite(boolean adding, Recipe recipe, User user){
        if(adding){
            //add the recipe to the user's favourites
            Log.w(TAG, "adding favourite...");
            new FavouriteCom(this, new ServerComTask.OnResponseListener<ServerMessage>(){
                @Override
                public void onResponse(ServerMessage message) {
                    switch (message.getStatus()){
                        case ServerMessage.SUCCESS:
                            //TODO: change the icon to highlighted or change it to grey depending on the current set image.
                            break;

                        case ServerMessage.ERROR:
                            //TODO: Toast with failure.
                            break;
                    }
                }
            }, FavouriteCom.ADD, recipe.getRecipeId(), user.getHash());
        }

    }

    private void showLoginDialog(){
        /* Alert dialog */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

            /* Build Alert dialog*/
        builder.setMessage(R.string.sign_in_text) /*Set text description*/
                .setPositiveButton(R.string.common_signin_button_text, new DialogInterface.OnClickListener() {         /*Sign in button*/
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO go to sign in fragment
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


    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Show the favourite button, is dependent on "isDrawerOpen()"
        menu.findItem(R.id.favourite_button).setVisible(!this.isDrawerOpen());

        super.onPrepareOptionsMenu(menu);

        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int headerHeight = getResources().getDimensionPixelSize(R.dimen.recipe_image_height);
        float speed = 0.75f; // less than 1 will make the underlying views scroll behind the header before it reaches action bar height
        int offset = (int)(y * speed);
        RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
        header.getLayoutParams().height = Math.max(headerHeight - offset, this.getActionBarHeight());
        header.requestLayout();
    }
}
