package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jesper on 12-03-14.
 */
public class RecipeFragment extends Fragment {

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

        LinearLayout ingredientGroupsLayout = (LinearLayout) rootView.findViewById(R.id.ingredientGroupsLinearLayout);
        for (IngredientGroup ingredientGroup : this.recipe.getIngredient()) {
            TextView ingredientGroupHeader = new TextView(this.getActivity());
            ingredientGroupHeader.setText(ingredientGroup.getName());
            ingredientGroupsLayout.addView(ingredientGroupHeader);

            RecipeIngredientGroupList ingredientGroupList = new RecipeIngredientGroupList(this.getActivity());
            for (ExchangeableIngredient exchangeableIngredient : ingredientGroup.getExchangeableIngredients()) {
                ingredientGroupList.addView(exchangeableIngredient);
            }
        }

        this.instructionList = (InstructionList) rootView.findViewById(R.id.instructionList);
        for (InstructionStep instructionStep : this.recipe.getInstructionSteps()) {
            this.instructionList.addView(instructionStep);
        }

        Spinner fontSpinner = (Spinner) rootView.findViewById(R.id.fontSpinner);
        fontSpinner.setSelection(0); // first element is default size
        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = RecipeFragment.this.getResources().getStringArray(R.array.font_sizes)[position];
                float size = Float.valueOf(selected.substring(0,2));
                RecipeFragment.this.instructionList.setFontSize(size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // CAN THIS HAPPEN?!?!
            }
        });

        RecipeCommentList commentList = (RecipeCommentList) rootView.findViewById(R.id.recipeCommentList);
        for (Comment comment : this.recipe.getComments()) {
            commentList.addView(comment);
        }

        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu
        super.onPrepareOptionsMenu(menu);
    }
}
