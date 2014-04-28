package aau.sw8.recipe;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import aau.sw8.data.IngredientCom;
import aau.sw8.data.ServerComTask;
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
    private RecipeSearchFragment.OnFragmentInteractionListener interactionListener;

    public SearchFragment() {
        // Required empty public constructor
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
        exchangeableIngredients1.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(new IngredientQuantity(0L, new Ingredient(0L, "cream", "cream", null), new Unit(0L, "ml", "ml", 1f), 480d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(new IngredientQuantity(0L, new Ingredient(0L, "dark chocolate, chopped", "dark chocolate, chopped", null), new Unit(0L, "g", "g", 1f), 120d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(new IngredientQuantity(0L, new Ingredient(0L, "earl grey tea", "earl grey tea", null), new Unit(0L, "bags", "bags", 1f), 2d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(new IngredientQuantity(0L, new Ingredient(0L, "egg yolk", "egg yolk", null), null, 6d))), true));
        exchangeableIngredients1.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(new IngredientQuantity(0L, new Ingredient(0L, "sugar", "sugar", null), new Unit(0L, "tablespoons", "tablespoons", 1f), 3))), true));

        ArrayList<ExchangeableIngredient> exchangeableIngredients2 = new ArrayList<>();
        exchangeableIngredients2.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(new IngredientQuantity(0L, new Ingredient(0L, "whipping cream", "whipping cream", null), new Unit(0L, "ml", "ml", 1f), 120d))), true));
        exchangeableIngredients2.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(new IngredientQuantity(0L, new Ingredient(0L, "powdered sugar", "powdered sugar", null), new Unit(0L, "tablespoon", "tablespoon", 1f), 2d))), true));
        exchangeableIngredients2.add(new ExchangeableIngredient(0L, new ArrayList<IngredientQuantity>(Arrays.asList(
                new IngredientQuantity(0L, new Ingredient(0L, "cointreau", "cointreau", null), new Unit(0L, "teaspoon", "teaspoon", 1f), 3d),
                new IngredientQuantity(0L, new Ingredient(0L, "hest", "heste", null), new Unit(0L, "kg", "kg", 1f), 5d))), true));

        ArrayList<IngredientGroup> ingredientGroups = new ArrayList<>();
        ingredientGroups.add(new IngredientGroup(0L, "The puddings", exchangeableIngredients1));
        ingredientGroups.add(new IngredientGroup(0L, "Cointreau cream", exchangeableIngredients2));

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

        new IngredientCom((DrawerActivity) super.getActivity(), new ServerComTask.OnResponseListener<ArrayList<Ingredient>>() {
            @Override
            public void onResponse(ArrayList<Ingredient> result) {
                for (Ingredient ingredient : result) {
                    //TODO sort ingredients with Sam's fancy algorithm
                    final IngredientButton ingredientButton = new IngredientButton(SearchFragment.super.getActivity());
                    ingredientButton.setIngredient(ingredient);

                    ingredientButton.setIngredientButtonClickListener(new IngredientButton.IngredientButtonClickListener() {
                        @Override
                        public void OnSelectedChanged(boolean isSelected) {
                            SearchFragment.this.ingredientFlowLayout.removeView(ingredientButton);
                            if (isSelected) {
                                SearchFragment.this.ingredientFlowLayout.addView(ingredientButton, 0);
                            } else {
                                SearchFragment.this.ingredientFlowLayout.addView(ingredientButton);
                            }
                        }

                        @Override
                        public void OnHighlightedChanged(boolean isHighlighted) {
                            //TODO handle
                        }
                    });
                    SearchFragment.this.ingredientFlowLayout.addView(ingredientButton);
                }
            }
        });

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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        super.getActivity().getMenuInflater().inflate(R.menu.main, menu);
        MainActivity mainActivity = (MainActivity) this.getActivity();
        menu.findItem(R.id.ingredient_search).setVisible(!mainActivity.isDrawerOpen());

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchBar = (SearchView) menu.findItem(R.id.ingredient_search).getActionView();
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchBar.setIconifiedByDefault(false);

        // Set hint text
        searchBar.setQueryHint(getString(R.string.ingredient_search_hint));

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHasOptionsMenu(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
