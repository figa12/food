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


public class SearchFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    private OnFragmentInteractionListener interactionListener;

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

        // TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        RecipeList recipeList = (RecipeList) rootView.findViewById(R.id.recipeList);
        // temporary display.test code
        for(int j = 0; j < 3; j++) {
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/17b8d0860fc30be68222cc5cfb53f399.jpg", "Russian Salad"));
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/178cd176ce2294f620beb3d943f354e1.jpg", "Avocado Mango Salad"));
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/7a0a46455c4ec56a5a02c097374fc513.jpg", "Chocolate Earl Grey"));
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/b39f2dfcc3325121c204028cca430743.jpg", "Steamed tofu"));
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/8e38d6eb334fb71b75bf9941afde58e0.jpg", "Thai beef curry"));
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/b614b1b2d1e412459a6390b950d16f14.jpg", "English Biscuits"));
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/4c3211edef29df58e40944e09374e691.jpg", "Cream Salad Dressing og noget meget langt"));
        }

        return rootView;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (this.interactionListener != null) {
            this.interactionListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
