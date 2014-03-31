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

import java.util.ArrayList;

import aau.sw8.model.InstructionStep;
import aau.sw8.model.Recipe;


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

        for(int j = 0; j < 3; j++) {
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/17b8d0860fc30be68222cc5cfb53f399.jpg", "Russian Salad"));
                recipeList.addView(new Recipe("http://www.opensourcefood.com/public/images/cached/567x/recipe_images/178cd176ce2294f620beb3d943f354e1.jpg", "Avocado Mango Salad"));
                recipeList.addView(chocoYumYum);
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
