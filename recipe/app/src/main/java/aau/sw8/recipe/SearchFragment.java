package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import aau.sw8.model.ExchangeableIngredient;
import aau.sw8.model.Ingredient;
import aau.sw8.model.IngredientGroup;
import aau.sw8.model.IngredientQuantity;
import aau.sw8.model.InstructionStep;
import aau.sw8.model.Recipe;
import aau.sw8.model.Unit;


public class SearchFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    private FlowLayout ingredientFlowLayout;
    private LinearLayout popupLayout;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        // TODO: make it possible to change the pagetitle according to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        SearchList recipeList = (SearchList) rootView.findViewById(R.id.searchList);
        // temporary display.test code

        Recipe chocoYumYum = new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/7a0a46455c4ec56a5a02c097374fc513.jpg", "Chocolate Earl Grey");
        chocoYumYum.setRecipeDescription("Pots de Creme is a thick and rich pudding that's baked and served chilled. This one uses earl gray tea in addition to chocolate to give it a more fragrant touch. I served it with a Cointreau ( an orange-flavored liqueur) whipped cream.\nThis recipe makes about 6  3/4 cup ramkins or  3-4 mugs.");
        ArrayList<InstructionStep> instructionSteps = new ArrayList<>();
        instructionSteps.add(new InstructionStep("Preheat your oven to 325 Fahrenheit with a rack in the center.", 1));
        instructionSteps.add(new InstructionStep("Heat cream in a pan over medium with the tea bags fully submerged until the cream is almost simmering. Try to keep them moving, so the tea can infuse the cream. Press and remove the tea bags before adding the chocolate and stirring until fully blended.", 2));
        instructionSteps.add(new InstructionStep("Mix your yolks with the sugar until smooth, then add your hot cream mixture gradually, whisking together. Set your ramkins or mugs onto a baking pan. Distribute the pudding evenly and then add hot water to the pan until it comes about halfway up the pudding dishes.", 3));
        instructionSteps.add(new InstructionStep("Bake for about 25 minutes, or until the top and edges set.", 4));
        instructionSteps.add(new InstructionStep("Let cool and refrigerate for at least 2 hours before adding the whipped cream, any desired garnish and serving.", 5, "http://www.opensourcefood.com/public/images/cached/567x/recipe_images/7a0a46455c4ec56a5a02c097374fc513.jpg"));
        chocoYumYum.setInstructionSteps(instructionSteps);

        ArrayList<ExchangeableIngredient> exchangeableIngredients1 = new ArrayList<>();
        exchangeableIngredients1.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(new IngredientQuantity(0, new Ingredient(0, "cream", "cream", null), new Unit(0, "ml", "ml", 1f), 480d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(new IngredientQuantity(0, new Ingredient(0, "dark chocolate, chopped", "dark chocolate, chopped", null), new Unit(0, "g", "g", 1f), 120d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(new IngredientQuantity(0, new Ingredient(0, "earl grey tea", "earl grey tea", null), new Unit(0, "bags", "bags", 1f), 2d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(new IngredientQuantity(0, new Ingredient(0, "egg yolk", "egg yolk", null), null, 6d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(new IngredientQuantity(0, new Ingredient(0, "sugar", "sugar", null), new Unit(0, "tablespoons", "tablespoons", 1f), 3))), true));

        ArrayList<ExchangeableIngredient> exchangeableIngredients2 = new ArrayList<>();
        exchangeableIngredients2.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(new IngredientQuantity(0, new Ingredient(0, "whipping cream", "whipping cream", null), new Unit(0, "ml", "ml", 1f), 120d))), true));
        exchangeableIngredients2.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(new IngredientQuantity(0, new Ingredient(0, "powdered sugar", "powdered sugar", null), new Unit(0, "tablespoon", "tablespoon", 1f), 2d))), true));
        exchangeableIngredients2.add(new ExchangeableIngredient(0, null, new ArrayList<>(Arrays.asList(
                new IngredientQuantity(0, new Ingredient(0, "cointreau", "cointreau", null), new Unit(0, "teaspoon", "teaspoon", 1f), 3d),
                new IngredientQuantity(0, new Ingredient(0, "hest", "heste", null), new Unit(0, "kg", "kg", 1f), 5d))), true));

        ArrayList<IngredientGroup> ingredientGroups = new ArrayList<>();
        ingredientGroups.add(new IngredientGroup(0, "The puddings", exchangeableIngredients1));
        ingredientGroups.add(new IngredientGroup(0, "Cointreau cream", exchangeableIngredients2));

        chocoYumYum.setIngredientGroups(ingredientGroups);

        for(int j = 0; j < 3; j++) {
            recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/17b8d0860fc30be68222cc5cfb53f399.jpg", "Russian Salad"));
            recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/178cd176ce2294f620beb3d943f354e1.jpg", "Avocado Mango Salad"));
            recipeList.addView(chocoYumYum);
            recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/b39f2dfcc3325121c204028cca430743.jpg", "Steamed tofu"));
            recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/8e38d6eb334fb71b75bf9941afde58e0.jpg", "Thai beef curry"));
            recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/b614b1b2d1e412459a6390b950d16f14.jpg", "English Biscuits"));
            recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/4c3211edef29df58e40944e09374e691.jpg", "Cream Salad Dressing og noget meget langt"));
        }

        this.ingredientFlowLayout = (FlowLayout) rootView.findViewById(R.id.ingredientsFlowLayout);

        for (int i = 0; i < 10; i++) {
            IngredientButton hest = new IngredientButton(this.getActivity());
            hest.setText("Hestsdfff " + String.valueOf(i));
            hest.setIngredientButtonClickListener(new IngredientButton.IngredientButtonClickListener() {
                @Override
                public void OnSelectedChanged(boolean isSelected) {
                    //TODO handle
                }

                @Override
                public void OnHighlightedChanged(boolean isHighlighted) {
                    //TODO handle
                }
            });
            this.ingredientFlowLayout.addView(hest);
        }

        this.popupLayout = (LinearLayout) rootView.findViewById(R.id.popupLayout);

        KeyboardEventsLayout keyboardEventsLayout = (KeyboardEventsLayout) rootView.findViewById(R.id.keyboardEventsLayout);
        keyboardEventsLayout.setOnKeyboardVisibilityChangedListener(new KeyboardEventsLayout.OnKeyboardVisibilityChangedListener() {
            @Override
            public void OnVisibilityChanged(boolean isVisible) {
                if (isVisible) {
                    // keyboard shown
                    SearchFragment.this.popupLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchFragment.this.getActivity(), "Keyboard shown", Toast.LENGTH_SHORT).show();
                } else {
                    // keyobard hidden
                    SearchFragment.this.popupLayout.setVisibility(View.GONE);
                    Toast.makeText(SearchFragment.this.getActivity(), "Keyboard hidden", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        menu.findItem(R.id.action_search).setVisible(!mainActivity.isDrawerOpen());

        SearchView searchBar = ((SearchView)menu.findItem(R.id.action_search).getActionView());

        // Default to search field
        searchBar.setIconifiedByDefault(false);

        // Set hint text
        searchBar.setQueryHint(getString(R.string.ingredient_search_hint));

        super.onPrepareOptionsMenu(menu);
    }

}
