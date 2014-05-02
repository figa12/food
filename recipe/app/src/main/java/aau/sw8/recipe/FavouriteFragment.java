package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;

import aau.sw8.data.FavouriteListCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.IntermediateRecipe;

/**
 * Created by jacob on 3/25/14.
 */
public class FavouriteFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_FAVOURITE = "favourite";

    private ProgressBar progressCircle;
    private FavouriteList favouriteList;
    private int limit = 10;
    private int offset = 0;

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
        View rootView = inflater.inflate(R.layout.fragment_favourite_list, container, false);

        this.favouriteList = (FavouriteList) rootView.findViewById(R.id.favouriteList);
        this.progressCircle = (ProgressBar) rootView.findViewById(R.id.progressCircleFavouriteFragment);

        /*Gets favourites from the database*/
        //TODO: Load the favourite list from the database
        getFavourites(LogInActivity.user.getHash(), this.limit, this.offset, "us");

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
    public void onResume() {
        super.onResume();

        getFavourites(LogInActivity.user.getHash(), limit, offset, "us");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu
        super.onPrepareOptionsMenu(menu);
    }

    private void getFavourites(String hash, int limit, int offset, String lang){
        this.progressCircle.setVisibility(View.VISIBLE);
        new FavouriteListCom((DrawerActivity) this.getActivity(), new ServerComTask.OnResponseListener<ArrayList<IntermediateRecipe>>() {
            @Override
            public void onResponse(ArrayList<IntermediateRecipe> result) {
                FavouriteFragment.this.loadFavourites(result);
                FavouriteFragment.this.progressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onFailed() {
                FavouriteFragment.this.progressCircle.setVisibility(View.GONE);
            }
        },FavouriteListCom.GET, hash, limit, offset, lang);
    }

    private void loadFavourites(ArrayList<IntermediateRecipe> intermediateRecipes){
        this.favouriteList.removeAllViews();
        for (IntermediateRecipe intermediateRecipe : intermediateRecipes){
            this.favouriteList.addView(intermediateRecipe);
        }
    }
}
