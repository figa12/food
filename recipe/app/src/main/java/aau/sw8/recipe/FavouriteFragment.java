package aau.sw8.recipe;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import aau.sw8.data.FavouriteListCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.IntermediateRecipe;

/**
 * Created by jacob on 3/25/14.
 */
public class FavouriteFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_FAVOURITE = "favourite";

    private FrameLayout progressContainer;
    private ProgressBar progressCircle;

    private FavouriteList favouriteList;

    private boolean moreRecipesAvailable = true;
    private boolean searchActive = false;

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

        this.progressContainer = (FrameLayout) rootView.findViewById(R.id.favProgressContainer);
        this.progressCircle = (ProgressBar) rootView.findViewById(R.id.favProgressCircle);

        /*Gets favourites from the database*/
        //TODO: Load the favourite list from the database
        if(LogInActivity.user != null) {
            this.getFavourites(LogInActivity.user.getHash());
        }else{
            ((DrawerActivity)this.getActivity()).selectItem(2);
        }

        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        //TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        ObservableScrollView observableScrollView = (ObservableScrollView) rootView.findViewById(R.id.scrollView);
        observableScrollView.setOnBottomReachedListener(new ObservableScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                FavouriteFragment.this.getMoreFavourites(FavouriteFragment.this.favouriteList.getSize(), LogInActivity.user.getHash());
            }
        });

        MainActivity mainActivity = (MainActivity) this.getActivity();
        ActionBar actionBar = mainActivity.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);

        return rootView;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();

        if(LogInActivity.user != null){
            getFavourites(LogInActivity.user.getHash());
            favouriteList.clearHighlight();
        }else{
            ((DrawerActivity)this.getActivity()).selectItem(2);
        }
    }

    private void getFavourites(String hash){
        //assume more recipes are available to download
        this.moreRecipesAvailable = true;

        // this is a new search, clear the search list and search with offset 0
        this.favouriteList.removeAllViews();
        this.getMoreFavourites(0, hash);
    }

    private void getMoreFavourites(int offset, String hash) {
        if (!this.moreRecipesAvailable || this.searchActive) {
            return;
        }

        this.searchActive = true;
        this.progressContainer.setVisibility(View.VISIBLE);
        this.progressCircle.setVisibility(View.VISIBLE);

        new FavouriteListCom((DrawerActivity) this.getActivity(), new ServerComTask.OnResponseListener<ArrayList<IntermediateRecipe>>() {
            @Override
            public void onResponse(ArrayList<IntermediateRecipe> result) {
                for (IntermediateRecipe intermediateRecipe : result)
                    FavouriteFragment.this.favouriteList.addView(intermediateRecipe);

                FavouriteFragment.this.onSearchComplete(result.size() > 0);
            }

            @Override
            public void onFailed() {
                FavouriteFragment.this.onSearchComplete(false);
            }
        }, FavouriteListCom.GET, hash, RecipeList.LIMIT, offset, "us");
    }

    private void onSearchComplete(boolean moreRecipesAvailable) {
        this.moreRecipesAvailable = moreRecipesAvailable;
        this.searchActive = false;

        this.progressCircle.setVisibility(View.GONE);
        if (moreRecipesAvailable) {
            this.progressContainer.setVisibility(View.GONE);
        }
    }

    private void loadFavourites(ArrayList<IntermediateRecipe> intermediateRecipes){
        this.favouriteList.removeAllViews();
        for (IntermediateRecipe intermediateRecipe : intermediateRecipes){
            this.favouriteList.addView(intermediateRecipe);
        }
    }
}
