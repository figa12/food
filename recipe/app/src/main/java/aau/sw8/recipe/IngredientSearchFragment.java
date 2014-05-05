package aau.sw8.recipe;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aau.sw8.data.IngredientCom;
import aau.sw8.data.IngredientSearchCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.Ingredient;
import aau.sw8.model.IntermediateRecipe;


public class IngredientSearchFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    private FlowLayout ingredientFlowLayout;
    private LinearLayout popupLayout;
    public static ArrayList<Ingredient> allIngredients = new ArrayList<>();
    private OnFragmentInteractionListener interactionListener;
    private SearchView searchBar;
    private int i = 0;
    private ProgressBar progressCircle;

    private SearchList searchList;

    public IngredientSearchFragment() {
        // Required empty public constructor
    }

    public static Ingredient getIngredient(long id) {
        for (Ingredient ingredient : IngredientSearchFragment.allIngredients) {
            if (ingredient.getId() == id) {
                return ingredient;
            }
        }
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (i == 0) {
            try {
                i++;
                getAllIngredients(menu);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

        this.progressCircle = (ProgressBar) rootView.findViewById(R.id.progressCircle);

        this.searchForRecipes(new ArrayList<Long>(Arrays.asList(1L)));

        this.ingredientFlowLayout = (FlowLayout) rootView.findViewById(R.id.ingredientsFlowLayout);

       /* new IngredientCom((DrawerActivity) super.getActivity(), new ServerComTask.OnResponseListener<ArrayList<Ingredient>>() {
            @Override
            public void onResponse(ArrayList<Ingredient> result) {
                for (Ingredient ingredient : result) {
                    //TODO sort ingredients with Sam's fancy algorithm
                   final IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());
                    ingredientButton.setIngredient(ingredient);

                    ingredientButton.setIngredientButtonClickListener(new IngredientButton.IngredientButtonClickListener() {
                        @Override
                        public void OnSelectedChanged(boolean isSelected) {
                            IngredientSearchFragment.this.ingredientFlowLayout.removeView(ingredientButton);
                            if (isSelected) {
                                IngredientSearchFragment.this.ingredientFlowLayout.addView(ingredientButton, 0);
                            } else {
                                IngredientSearchFragment.this.ingredientFlowLayout.addView(ingredientButton);
                            }
                        }

                        @Override
                        public void OnHighlightedChanged(boolean isHighlighted) {
                            //TODO handle
                        }
                    });
                    IngredientSearchFragment.this.ingredientFlowLayout.addView(ingredientButton);
                }
            }
            @Override
            public void onFailed() { }
        });*/

        this.popupLayout = (LinearLayout) rootView.findViewById(R.id.popupLayout);

        KeyboardEventsLayout keyboardEventsLayout = (KeyboardEventsLayout) rootView.findViewById(R.id.keyboardEventsLayout);
        keyboardEventsLayout.setOnKeyboardVisibilityChangedListener(new KeyboardEventsLayout.OnKeyboardVisibilityChangedListener() {
            @Override
            public void OnVisibilityChanged(boolean isVisible) {
                if (isVisible) {
                    // keyboard shown
                    IngredientSearchFragment.this.popupLayout.setVisibility(View.VISIBLE);
                } else {
                    // keyboard hidden
                }
            }
        });

        return rootView;
    }

    private void searchForRecipes(ArrayList<Long> ingredientIds) {
        this.progressCircle.setVisibility(View.VISIBLE);

        new IngredientSearchCom((DrawerActivity) this.getActivity(), new ServerComTask.OnResponseListener<ArrayList<IntermediateRecipe>>() {
            @Override
            public void onResponse(ArrayList<IntermediateRecipe> result) {
                IngredientSearchFragment.this.displayRecipeList(result);
                IngredientSearchFragment.this.progressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onFailed() {
                IngredientSearchFragment.this.progressCircle.setVisibility(View.GONE);
            }
        }, ingredientIds);
    }

    private void displayRecipeList(ArrayList<IntermediateRecipe> intermediateRecipes) {
        this.searchList.removeAllViews();
        for (IntermediateRecipe intermediateRecipe : intermediateRecipes)
            this.searchList.addView(intermediateRecipe);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        super.getActivity().getMenuInflater().inflate(R.menu.main, menu);
        MainActivity mainActivity = (MainActivity) this.getActivity();

        Button searchButton = (Button) getActivity().findViewById(R.id.searchButton);
        searchButton.setText("Search");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientSearchFragment.this.popupLayout.setVisibility(View.GONE);
                ((MainActivity) IngredientSearchFragment.this.getActivity()).dismissKeyboard();
                ArrayList<Long> searchResult = new ArrayList<>();

                for(int i = 0; i < IngredientSearchFragment.this.ingredientFlowLayout.getChildCount(); i++) {

                    if(IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i).isSelected()) {
                        IngredientButton tempButton = (IngredientButton) IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i);
                        searchResult.add(tempButton.getIngredient().getId());
                    }
                }

                if(searchResult.isEmpty())
                    Toast.makeText(getActivity(), "Please enter some ingredients", Toast.LENGTH_SHORT).show();
                else
                    searchForRecipes(searchResult);
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchBar = (SearchView) menu.findItem(R.id.ingredient_search).getActionView();
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchBar.setQueryHint(getString(R.string.ingredient_search_hint));
        searchBar.setIconifiedByDefault(false);

        if (allIngredients.size() != 0) {
            menu.findItem(R.id.ingredient_search).setVisible(!mainActivity.isDrawerOpen());
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.

                updateFlowLayout(query);

                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);

        super.onPrepareOptionsMenu(menu);
    }

    private void getAllIngredients(final Menu menu) throws IOException {

        final MainActivity mainActivity = (MainActivity) this.getActivity();

        new IngredientCom((DrawerActivity) super.getActivity(), new ServerComTask.OnResponseListener<ArrayList<Ingredient>>() {
            @Override
            public void onResponse(ArrayList<Ingredient> result) {
                for (Ingredient ingredient : result) {
                    allIngredients.add(ingredient);
                }

                menu.findItem(R.id.ingredient_search).setVisible(!mainActivity.isDrawerOpen());

            }

            @Override
            public void onFailed() {
            }
        });

    }

    public void updateFlowLayout() {

        final IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());

        for (Ingredient ingredient : allIngredients) {
            if (ingredient.getSingular().contains(MainActivity.ingredientResult)) {
                if (IngredientSearchFragment.this.ingredientFlowLayout.getChildCount() == 0) {

                    addButton(ingredientButton, ingredient);
                    break;

                } else {
                    testIfExists(ingredientButton, ingredient);
                    break;
                }
            }
        }
    }

    public void updateFlowLayout(String query){

        Pattern p = Pattern.compile("(^|\\s)" + query);

        final IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());

        for (Ingredient ingredient : allIngredients) {

            Matcher matcher = p.matcher(ingredient.getSingular().toLowerCase());

            if(matcher.find()) {

                if (IngredientSearchFragment.this.ingredientFlowLayout.getChildCount() == 0) {

                    addButton(ingredientButton, ingredient);

                    break;

                } else {

                    testIfExists(ingredientButton, ingredient);
                    break;
                }
            }
        }
    }

    private void testIfExists(IngredientButton ingredientButton, Ingredient ingredient) {

        boolean test = false;
        boolean f;

        for (int i = 0; i < IngredientSearchFragment.this.ingredientFlowLayout.getChildCount(); i++) {

            IngredientButton testButton = (IngredientButton) IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i);
            ingredientButton.setIngredient(ingredient);
            f = ingredientButton.equals(testButton);

            if (f) {
                test = f;
            }
        }
        if (!test) {
            addButton(ingredientButton, ingredient);
        }
        else  {
            Toast.makeText(getActivity(), "This ingredient has already been added", Toast.LENGTH_SHORT).show();
        }
    }

    private void addButton(final IngredientButton ingredientButton, Ingredient ingredient) {
        ingredientButton.setIngredient(ingredient);

        ingredientButton.setIngredientButtonClickListener(new IngredientButton.IngredientButtonClickListener() {

            @Override
            public void OnSelectedChanged(boolean isSelected) {
                IngredientSearchFragment.this.ingredientFlowLayout.removeView(ingredientButton);

                if (isSelected) {
                    IngredientSearchFragment.this.ingredientFlowLayout.addView(ingredientButton, 0);
                } else {
                    IngredientSearchFragment.this.ingredientFlowLayout.addView(ingredientButton);
                }
            }

            @Override
            public void OnHighlightedChanged(boolean isHighlighted) {
                //TODO handle
            }
        });

        IngredientSearchFragment.this.ingredientFlowLayout.addView(ingredientButton);
        ingredientButton.setSelected(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHasOptionsMenu(true);

        try {
            this.interactionListener = (OnFragmentInteractionListener) activity;
            this.interactionListener.onFragmentInteraction(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener"); }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.interactionListener.onFragmentInteraction(null);
        this.interactionListener = null;

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(IngredientSearchFragment ingredientSearchFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchList.clearHighlight();
    }
}