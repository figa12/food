package aau.sw8.recipe;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aau.sw8.data.IngredientCom;
import aau.sw8.data.IngredientSearchCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.Ingredient;
import aau.sw8.model.IntermediateRecipe;


public class IngredientSearchFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    private View rootView;
    private FlowLayout ingredientFlowLayout;
    public  LinearLayout popupLayout;
    public static ArrayList<Ingredient> allIngredients = new ArrayList<>();
    private OnFragmentInteractionListener interactionListener;
    public EditText searchBar;
    private ListView suggestionList;
    private FrameLayout suggestionWrapper;
    private int i;
    private ArrayList<String> suggestionName = new ArrayList<>();

    private FrameLayout progressContainer;
    private ProgressBar progressCircle;

    private SearchList searchList;
    private ArrayList<Ingredient> ingredients;
    private boolean moreRecipesAvailable = true;
    private boolean searchActive = false;

    private int noKeyboardHeight;
    private int oldHeight;

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

        if (allIngredients.size() == 0) {
            try {
                getAllIngredients();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        // TODO: make it possible to change the pagetitle according to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        this.searchList = (SearchList) rootView.findViewById(R.id.searchList);

        this.progressContainer = (FrameLayout) rootView.findViewById(R.id.progressContainer);
        this.progressCircle = (ProgressBar) rootView.findViewById(R.id.progressCircle);

       // this.searchForRecipes(new ArrayList<Long>(Arrays.asList(1L)));

        this.ingredientFlowLayout = (FlowLayout) rootView.findViewById(R.id.ingredientsFlowLayout);

        this.popupLayout = (LinearLayout) rootView.findViewById(R.id.popupLayout);
        this.suggestionList = (ListView) rootView.findViewById(R.id.search_suggestion);

        this.suggestionWrapper = (FrameLayout) rootView.findViewById(R.id.search_suggestion_wrapper);
        this.suggestionWrapper.setVisibility(View.INVISIBLE);

        for (Ingredient ingredient : allIngredients) {
            IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());

            addButton(ingredientButton, ingredient);
        }

        this.suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                String listItem = ((TextView) view).getText().toString();

               //       addIngredientToFlowLayout(listItem);
                        selectButton(true, listItem);
                        searchBar.setText("");

            }
        });

        ObservableScrollView observableScrollView = (ObservableScrollView) rootView.findViewById(R.id.oberservableScrollView);
        observableScrollView.setOnBottomReachedListener(new ObservableScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if (IngredientSearchFragment.this.ingredients != null) {
                    IngredientSearchFragment.this.searchForMoreRecipes(IngredientSearchFragment.this.searchList.getSize());
                }
            }
        });

        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();

        ActionBar actionBar = mainActivity.getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.menu_search_item);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

        searchBar = (EditText) actionBar.getCustomView().findViewById(R.id.menu_search);

        if (ingredients != null) {
            searchBar.setHint(getIngredientString(ingredients));
        }
        else {
            searchBar.setHint(R.string.search_hint);
        }

        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        // update suggestions when search bar text changes
        searchBar.addTextChangedListener(new TextWatcher() {
            private boolean isEmpty = true;

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if ((searchBar.length() == 0) != isEmpty) {
                    isEmpty = searchBar.length() == 0;

                    suggestionWrapper.setVisibility(View.INVISIBLE);
                    suggestionName.clear();
                    searchBar.setInputType(EditorInfo.TYPE_NULL); // force keyboard update

                    if (isEmpty)
                        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                    else
                        searchBar.setImeOptions(EditorInfo.IME_ACTION_NEXT);

                    searchBar.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); // force keyboard update
                }

                if (searchBar.length() != 0) {
                    suggestionName.clear();

                    Pattern p = Pattern.compile("(^|\\s)" + editable);

                    for (Ingredient ingredient : allIngredients) {
                        Matcher matcher = p.matcher(ingredient.getSingular().toLowerCase());

                        if (matcher.find()) {
                            suggestionName.add(ingredient.getSingular());
                        }
                    }

                    if (suggestionName.size() > 0)
                        suggestionWrapper.setVisibility(View.VISIBLE);
                    else
                        suggestionWrapper.setVisibility(View.GONE);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.search_list_item, suggestionName);
                suggestionList.setAdapter(arrayAdapter);
                suggestionList.setTextFilterEnabled(true);
            }
        });


        // search/add ingredient on enter
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int x, KeyEvent keyEvent) {
                if (searchBar.length() > 0) {
                    boolean ingredientMatched = addIngredientToFlowLayout(searchBar.getText().toString());

                    if (ingredientMatched) {
                        searchBar.setText("");
                    }
                    else {
                        Toast.makeText(getActivity(), "No matching ingredient was found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    ((MainActivity) IngredientSearchFragment.this.getActivity()).dismissKeyboard();


                    ArrayList<Ingredient> ingredients = getSelectedIngredient();

                    if (ingredients.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter some ingredients", Toast.LENGTH_SHORT).show();
                        searchBar.setHint(R.string.search_hint);
                    } else {
                        searchForRecipes(ingredients);

                        searchBar.setHint(getIngredientString(ingredients));
                    }
                }
                return true;
            }
        });

        if (allIngredients.size() != 0) {
            searchBar.setEnabled(!mainActivity.isDrawerOpen());
        }



        // keyboard visibility
        noKeyboardHeight = rootView.getHeight();
        oldHeight = rootView.getHeight();

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int fragmentHeight = rootView.getHeight();

                if (fragmentHeight != oldHeight) {
                    if (fragmentHeight < noKeyboardHeight) {
                        // keyboard shown
                        searchBar.setHint(R.string.search_hint);
                        popupLayout.setVisibility(View.VISIBLE);

                        if(suggestionList.getChildCount() == 0) {
                            suggestionWrapper.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        // keyboard hidden
                        if (ingredients != null) {
                            searchBar.setHint(getIngredientString(ingredients));
                        }
                        else {
                            searchBar.setHint(R.string.search_hint);
                        }

                        searchBar.clearFocus();
                        suggestionWrapper.setVisibility(View.GONE);
                        popupLayout.setVisibility(View.GONE);

                        // search on back
                        ArrayList<Ingredient> ingredients = getSelectedIngredient();
                        if (!ingredients.isEmpty()) {
                            searchForRecipes(ingredients);
                            searchBar.setHint(getIngredientString(ingredients));
                        }
                    }

                    oldHeight = fragmentHeight;
                }
            }
        });

        super.onPrepareOptionsMenu(menu);
    }


    private void searchForRecipes(ArrayList<Ingredient> ingredients) {
        // save the ingredient ids so we can use them to search for recipes when we reach the bottom
        this.ingredients = ingredients;

        //assume more recipes are available to download
        this.moreRecipesAvailable = true;

        // this is a new search, clear the search list and search with offset 0
        this.searchList.removeAllViews();
        this.searchForMoreRecipes(0);
    }

    private void searchForMoreRecipes(int offset) {
        if (!this.moreRecipesAvailable || this.searchActive) {
            return;
        }

        this.searchActive = true;
        this.progressContainer.setVisibility(View.VISIBLE);
        this.progressCircle.setVisibility(View.VISIBLE);

        ArrayList<Long> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : this.ingredients) {
            ingredientIds.add(ingredient.getId());
        }

        new IngredientSearchCom((DrawerActivity) this.getActivity(), new ServerComTask.OnResponseListener<ArrayList<IntermediateRecipe>>() {
            @Override
            public void onResponse(ArrayList<IntermediateRecipe> result) {
                for (IntermediateRecipe intermediateRecipe : result)
                    IngredientSearchFragment.this.searchList.addView(intermediateRecipe);

                IngredientSearchFragment.this.onSearchComplete(result.size() > 0);
            }

            @Override
            public void onFailed() {
                IngredientSearchFragment.this.onSearchComplete(false);
            }
        }, ingredientIds, offset, RecipeList.LIMIT);
    }

    private void onSearchComplete(boolean moreRecipesAvailable) {
        this.moreRecipesAvailable = moreRecipesAvailable;
        this.searchActive = false;

        this.progressCircle.setVisibility(View.GONE);
        if (moreRecipesAvailable) {
            this.progressContainer.setVisibility(View.GONE);
        }
    }


    // Get the ingredient that has been pressed by the user in the list of search suggestions
    private ArrayList<Ingredient> getSelectedIngredient() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        for (int i = 0; i < IngredientSearchFragment.this.ingredientFlowLayout.getChildCount(); i++) {

            if (IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i).isSelected()) {
                IngredientButton ingredientButton = (IngredientButton) IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i);
                ingredients.add(ingredientButton.getIngredient());
            }
        }

        return ingredients;
    }


    private void selectButton(boolean select, String query){

        for (int i = 0; i < IngredientSearchFragment.this.ingredientFlowLayout.getChildCount(); i++) {

            IngredientButton testButton = (IngredientButton) IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i);
           if(testButton.getIngredient().getSingular().equals(query)){
               if(testButton.isSelected()) {
                   Toast.makeText(getActivity(), "This ingredient has already been selected", Toast.LENGTH_SHORT).show();
                   break;
               }

               else {
                   testButton.setSelected(select);
                   break;
               }
           }
        }
    }

    private String getIngredientString(ArrayList<Ingredient> ingredients) {
        String ingredientsString = "";

        Iterator<Ingredient> it = ingredients.iterator();
        if (it.hasNext()) {
            ingredientsString += it.next().getSingular();
        }
        while (it.hasNext()) {
            ingredientsString += ", " + it.next().getSingular();
        }

        return ingredientsString;
    }

    private void getAllIngredients() throws IOException {

        final MainActivity mainActivity = (MainActivity) this.getActivity();

        new IngredientCom((DrawerActivity) super.getActivity(), new ServerComTask.OnResponseListener<ArrayList<Ingredient>>() {
            @Override
            public void onResponse(ArrayList<Ingredient> result) {
                for (Ingredient ingredient : result) {
                    allIngredients.add(ingredient);
                }


                for (Ingredient ingredient : allIngredients) {
                    IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());

                    addButton(ingredientButton, ingredient);
                }

                searchBar.setEnabled(!mainActivity.isDrawerOpen());

            }

            @Override
            public void onFailed() {
            }
        });

    }

    public boolean addIngredientToFlowLayout(String query){

        Pattern p = Pattern.compile("(^|\\s)" + query);

      //  IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());

        for (Ingredient ingredient : allIngredients) {
            Matcher matcher = p.matcher(ingredient.getSingular().toLowerCase());

            if(matcher.find()) {
             //  addButtonIfNotExists(ingredientButton, ingredient);
                selectButton(true, ingredient.getSingular());
                return true;
            }
        }

        return false;
    }

   /* private void addButtonIfNotExists(IngredientButton ingredientButton, Ingredient ingredient) {

        boolean test = false;

        for (int i = 0; i < IngredientSearchFragment.this.ingredientFlowLayout.getChildCount(); i++) {

            IngredientButton testButton = (IngredientButton) IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i);
            ingredientButton.setIngredient(ingredient);
            boolean found = ingredientButton.equals(testButton);

            if (found) {
                test = found;
            }
        }
        if (!test) {
            addButton(ingredientButton, ingredient);
        }
        else  {
            Toast.makeText(getActivity(), "This ingredient has already been added", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void addButton(final IngredientButton ingredientButton, Ingredient ingredient) {
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
        });

        IngredientSearchFragment.this.ingredientFlowLayout.addView(ingredientButton);

        //Set all buttons to not enabled on creation
        ingredientButton.setSelected(false);
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