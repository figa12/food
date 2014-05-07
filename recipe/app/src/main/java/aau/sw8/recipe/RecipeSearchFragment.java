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
    private ProgressBar progressCircle;

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

        this.progressCircle = (ProgressBar) rootView.findViewById(R.id.progressCircle);

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

        final EditText searchBar = (EditText) actionBar.getCustomView().findViewById(R.id.menu_search);

        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchBar.setEnabled(!mainActivity.isDrawerOpen());

        // Set hint text
        searchBar.setHint(getString(R.string.recipe_search_hint));

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int x, KeyEvent keyEvent) {
                searchResults(searchBar.getText().toString());
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

    public void searchResults(String query) {
        this.searchList.removeAllViews();
        this.progressCircle.setVisibility(View.VISIBLE);

        new RecipeSearchCom((DrawerActivity) this.getActivity(), new ServerComTask.OnResponseListener<ArrayList<IntermediateRecipe>>() {
            @Override
            public void onResponse(ArrayList<IntermediateRecipe> result) {
                RecipeSearchFragment.this.displayRecipeList(result);
                RecipeSearchFragment.this.progressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onFailed() {
                RecipeSearchFragment.this.progressCircle.setVisibility(View.GONE);
            }
        }, query);
    }

    private void displayRecipeList(ArrayList<IntermediateRecipe> intermediateRecipes) {
        for (IntermediateRecipe intermediateRecipe : intermediateRecipes)
            this.searchList.addView(intermediateRecipe);
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