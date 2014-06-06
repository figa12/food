package aau.sw8.recipe;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import aau.sw8.data.RecipeSearchCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.IntermediateRecipe;

/**
* Created by Reedtz on 31-03-2014.
*/
public class RecipeSearchFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    private OnFragmentInteractionListener interactionListener;
    private SearchList searchList;

    private FrameLayout progressContainer;
    private ProgressBar progressCircle;

    private String query;
    private boolean moreRecipesAvailable = true;
    private boolean searchActive = false;
    private LinearLayout startpage;

    public RecipeSearchFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        this.searchList = (SearchList) rootView.findViewById(R.id.searchList);
        // temporary display.test code

        this.startpage = (LinearLayout) rootView.findViewById(R.id.startPage);
        rootView.findViewById(R.id.recipeSearchTextView).setVisibility(View.VISIBLE);

        this.progressContainer = (FrameLayout) rootView.findViewById(R.id.progressContainer);
        this.progressCircle = (ProgressBar) rootView.findViewById(R.id.progressCircle);

        ObservableScrollView observableScrollView = (ObservableScrollView) rootView.findViewById(R.id.oberservableScrollView);
        observableScrollView.setOnBottomReachedListener(new ObservableScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if (RecipeSearchFragment.this.query != null) {
                    RecipeSearchFragment.this.searchForMoreRecipes(RecipeSearchFragment.this.searchList.getSize());
                }
            }
        });

        return rootView;
    }

    public void search(String query) {
        // save the ingredient ids so we can use them to search for recipes when we reach the bottom
        this.query = query;

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

        new RecipeSearchCom((DrawerActivity) this.getActivity(), new ServerComTask.OnResponseListener<ArrayList<IntermediateRecipe>>() {
            @Override
            public void onResponse(ArrayList<IntermediateRecipe> result) {
                for (IntermediateRecipe intermediateRecipe : result)
                    RecipeSearchFragment.this.searchList.addView(intermediateRecipe);

                RecipeSearchFragment.this.onSearchComplete(result.size() > 0);
            }

            @Override
            public void onFailed() {
                RecipeSearchFragment.this.onSearchComplete(false);
            }
        }, this.query, offset, RecipeList.LIMIT);
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

        if (mainActivity.isDrawerOpen())
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        else
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

        final EditText searchBar = (EditText) actionBar.getCustomView().findViewById(R.id.menu_search);

        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchBar.setEnabled(!mainActivity.isDrawerOpen());

        // Set hint text
        searchBar.setHint(getString(R.string.recipe_search_hint));

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int x, KeyEvent keyEvent) {
                search(searchBar.getText().toString());
                return true;
            }
        });

        if (!((DrawerActivity)getActivity()).isDrawerOpen()) {
            searchBar.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHasOptionsMenu(true);

        try {
            this.interactionListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener"); }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.interactionListener = null;
    }

/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchList.clearHighlight();
    }
}