package aau.sw8.recipe;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

/**
* Created by Reedtz on 31-03-2014.
*/
public class RecipeSearchFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    private OnFragmentInteractionListener interactionListener;
    private SearchList recipeList;

    public RecipeSearchFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        // TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        this.recipeList = (SearchList) rootView.findViewById(R.id.searchList);
        // temporary display.test code

        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        super.getActivity().getMenuInflater().inflate(R.menu.main, menu);
        MainActivity mainActivity = (MainActivity) this.getActivity();
        menu.findItem(R.id.ingredient_search).setVisible(!mainActivity.isDrawerOpen());

        SearchView searchBar = (SearchView) menu.findItem(R.id.ingredient_search).getActionView();
        searchBar.setIconifiedByDefault(false);

        // Set hint text
        searchBar.setQueryHint(getString(R.string.recipe_search_hint));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.

                searchResults(query);
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();

                return true;
            }
        };

        searchBar.setOnQueryTextListener(queryTextListener);

        super.onPrepareOptionsMenu(menu);
    }

    public void searchResults(String query){
        recipeList.removeAllViews();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (this.interactionListener != null) {
            this.interactionListener.onFragmentInteraction(uri);
        }
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
}