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
    public  LinearLayout popupLayout;
    public static ArrayList<Ingredient> allIngredients = new ArrayList<>();
    private OnFragmentInteractionListener interactionListener;
    public EditText searchBar;
    private ListView suggestionList;
    private FrameLayout suggestionWrapper;
    private int i = 0;

    private FrameLayout progressContainer;
    private ProgressBar progressCircle;

    private SearchList searchList;
    private ArrayList<Long> ingredientIds;
    private boolean moreRecipesAvailable = true;
    private boolean searchActive = false;

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

        this.progressContainer = (FrameLayout) rootView.findViewById(R.id.progressContainer);
        this.progressCircle = (ProgressBar) rootView.findViewById(R.id.progressCircle);

       // this.searchForRecipes(new ArrayList<Long>(Arrays.asList(1L)));

        this.ingredientFlowLayout = (FlowLayout) rootView.findViewById(R.id.ingredientsFlowLayout);

        this.popupLayout = (LinearLayout) rootView.findViewById(R.id.popupLayout);
        this.suggestionList = (ListView) rootView.findViewById(R.id.search_suggestion);

        this.suggestionWrapper = (FrameLayout) rootView.findViewById(R.id.search_suggestion_wrapper);
        this.suggestionWrapper.setVisibility(View.INVISIBLE);
        String[] list = {"Test", "Hej", "Fedt", "Ged"};


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.search_list_item, list);

        this.suggestionList.setAdapter(arrayAdapter);
        this.suggestionList.setTextFilterEnabled(true);


        this.suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                String listItem = ((TextView)view).getText().toString();
                //suggestionList.getItemAtPosition(position);
                Toast.makeText(getActivity(), listItem, Toast.LENGTH_SHORT).show();
            }
        });

        KeyboardEventsLayout keyboardEventsLayout = (KeyboardEventsLayout) rootView.findViewById(R.id.keyboardEventsLayout);
        keyboardEventsLayout.setOnKeyboardVisibilityChangedListener(new KeyboardEventsLayout.OnKeyboardVisibilityChangedListener() {
            @Override
            public void OnVisibilityChanged(boolean isVisible) {
                if (isVisible) {
                    // keyboard shown
                    IngredientSearchFragment.this.popupLayout.setVisibility(View.VISIBLE);
                    suggestionWrapper.setVisibility(View.VISIBLE);

                } else {
                    // keyboard hidden
                    suggestionWrapper.setVisibility(View.INVISIBLE);
                }
            }
        });

        ObservableScrollView observableScrollView = (ObservableScrollView) rootView.findViewById(R.id.oberservableScrollView);
        observableScrollView.setOnBottomReachedListener(new ObservableScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if (IngredientSearchFragment.this.ingredientIds != null) {
                    IngredientSearchFragment.this.searchForMoreRecipes(IngredientSearchFragment.this.searchList.getSize());
                }
            }
        });

        return rootView;
    }

    private void searchForRecipes(ArrayList<Long> ingredientIds) {
        // save the ingredient ids so we can use them to search for recipes when we reach the bottom
        this.ingredientIds = ingredientIds;

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
        }, this.ingredientIds, offset, RecipeList.LIMIT);
    }

    private void onSearchComplete(boolean moreRecipesAvailable) {
        this.moreRecipesAvailable = moreRecipesAvailable;
        this.searchActive = false;

        this.progressCircle.setVisibility(View.GONE);
        if (moreRecipesAvailable) {
            this.progressContainer.setVisibility(View.GONE);
        }
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
        searchBar.setHint("Enter an ingredient");

        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchBar.addTextChangedListener(new TextWatcher() {
            private boolean isEmpty = true;

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if ((searchBar.length() == 0) != isEmpty) {
                    isEmpty = searchBar.length() == 0;

                    searchBar.setInputType(EditorInfo.TYPE_NULL); // force keyboard update

                    if (isEmpty)
                        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                    else
                        searchBar.setImeOptions(EditorInfo.IME_ACTION_NEXT);

                    searchBar.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); // force keyboard update
                }
            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int x, KeyEvent keyEvent) {
                if (searchBar.length() > 0) {
                    addIngredientToFlowLayout(searchBar.getText().toString());
                    searchBar.setText("");
                }
                else {
                    IngredientSearchFragment.this.popupLayout.setVisibility(View.GONE);
                    ((MainActivity) IngredientSearchFragment.this.getActivity()).dismissKeyboard();
                    searchBar.clearFocus();

                    ArrayList<Long> ingredients = new ArrayList<>();

                    for (int i = 0; i < IngredientSearchFragment.this.ingredientFlowLayout.getChildCount(); i++) {

                        if (IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i).isSelected()) {
                            IngredientButton ingredientButton = (IngredientButton) IngredientSearchFragment.this.ingredientFlowLayout.getChildAt(i);
                            ingredients.add(ingredientButton.getIngredient().getId());
                        }
                    }

                    if (ingredients.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter some ingredients", Toast.LENGTH_SHORT).show();
                    } else {
                        searchForRecipes(ingredients);
                    }
                }
                return true;
            }
        });

        if (allIngredients.size() != 0) {
            searchBar.setEnabled(!mainActivity.isDrawerOpen());
        }

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

                searchBar.setEnabled(!mainActivity.isDrawerOpen());

            }

            @Override
            public void onFailed() {
            }
        });

    }

    public void updateFlowLayout() {

        final IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());

        for (Ingredient ingredient : allIngredients) {
            if (ingredient.getSingular().equals(MainActivity.ingredientResult)) {
                addButtonIfNotExists(ingredientButton, ingredient);
                break;
            }
        }
    }

    public void addIngredientToFlowLayout(String query){

        Pattern p = Pattern.compile("(^|\\s)" + query);

        IngredientButton ingredientButton = new IngredientButton(IngredientSearchFragment.super.getActivity());

        for (Ingredient ingredient : allIngredients) {
            Matcher matcher = p.matcher(ingredient.getSingular().toLowerCase());

            if(matcher.find()) {
                addButtonIfNotExists(ingredientButton, ingredient);
                break;
            }
        }
    }

    private void addButtonIfNotExists(IngredientButton ingredientButton, Ingredient ingredient) {

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
    }

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