package aau.sw8.recipe;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;


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

        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        RecipeList recipeList = (RecipeList) rootView.findViewById(R.id.recipeList);
        // temporary display.test code
        for(int j = 0; j < 20; j++)
            recipeList.addView(new Recipe("http://figz.dk/images/microsfot.jpeg", "Title", "Description description description"));

        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        menu.findItem(R.id.action_search).setVisible(!mainActivity.isDrawerOpen());
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
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
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
