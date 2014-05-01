package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import aau.sw8.model.IntermediateRecipe;

/**
 * Created by jacob on 3/25/14.
 */
public class FavouriteFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_FAVOURITE = "favourite";

    private String pageTitle;

    /* Contstructors */
    public FavouriteFragment() {
        /*Empty constructor*/
    }

    /* Override methods */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_favourite_list, container, false);

        FavouriteList favouriteList = (FavouriteList) rootView.findViewById(R.id.favouriteList);

        /*testdata*/
        for (int i = 1; i <= 10; i++) {
            favouriteList.addView(new IntermediateRecipe(1L, "Hest", "Hest", null, "hest"));
        }
        //TODO: Load the favourite list from the database
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        //TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        return rootView;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu
        super.onPrepareOptionsMenu(menu);
    }
}
