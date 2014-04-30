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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import aau.sw8.data.IngredientCom;
import aau.sw8.data.RecipeListCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.Ingredient;
import aau.sw8.model.IntermediateRecipe;


public class SearchFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    private FlowLayout ingredientFlowLayout;
    private LinearLayout popupLayout;
    public static ArrayList<Ingredient> allIngredients = new ArrayList<>();
    private RecipeSearchFragment.OnFragmentInteractionListener interactionListener;
    private SearchView searchBar;
    private int i = 0;

    private SearchList searchList;

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

        this.searchList = (SearchList) rootView.findViewById(R.id.searchList);

        new RecipeListCom((DrawerActivity) this.getActivity(), new ServerComTask.OnResponseListener<ArrayList<IntermediateRecipe>>() {
            @Override
            public void onResponse(ArrayList<IntermediateRecipe> result) {
                SearchFragment.this.displayRecipeList(result);
            }
            @Override
            public void onFailed() { }
        }, new ArrayList<Long>(Arrays.asList(1L, 5L)));

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
            @Override
            public void onFailed() { }
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
                    // keyboard hidden
                    SearchFragment.this.popupLayout.setVisibility(View.GONE);
                    Toast.makeText(SearchFragment.this.getActivity(), "Keyboard hidden", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void displayRecipeList(ArrayList<IntermediateRecipe> intermediateRecipes) {
        this.searchList.removeAllViews();
        for (IntermediateRecipe intermediateRecipe : intermediateRecipes)
            this.searchList.addView(intermediateRecipe);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if(i == 0) {
            try {
                i++;
                getAllIngredients(menu);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        
        // Inflate the menu; this adds items to the action bar if it is present.
        super.getActivity().getMenuInflater().inflate(R.menu.main, menu);
        MainActivity mainActivity = (MainActivity) this.getActivity();

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchBar = (SearchView) menu.findItem(R.id.ingredient_search).getActionView();
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchBar.setQueryHint(getString(R.string.ingredient_search_hint));
        searchBar.setIconifiedByDefault(false);

        if(allIngredients.size() != 0){
            menu.findItem(R.id.ingredient_search).setVisible(!mainActivity.isDrawerOpen());
        }

        super.onPrepareOptionsMenu(menu);
    }

    private void getAllIngredients(Menu menu) throws IOException {

        final MainActivity mainActivity = (MainActivity) this.getActivity();
        final Menu menu1 = menu;

        new IngredientCom((DrawerActivity) super.getActivity(), new ServerComTask.OnResponseListener<ArrayList<Ingredient>>() {
            @Override
            public void onResponse(ArrayList<Ingredient> result) {
                for (Ingredient ingredient : result) {
                    allIngredients.add(ingredient);
                }

                menu1.findItem(R.id.ingredient_search).setVisible(!mainActivity.isDrawerOpen());

            }
        });

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
