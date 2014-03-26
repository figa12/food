package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jacob on 3/25/14.
 */
public class FavouriteFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        // TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = "Favourites";
        super.getActivity().setTitle(this.pageTitle);

        FavouriteList favouriteList = (FavouriteList) rootView.findViewById(R.id.favouriteList);
        //TODO handle other views here

        /*testdata*/
        for (int i = 0; i <= 20; i++) {
            favouriteList.addView(new Recipe("http://figz.dk/images/microsfot.jpeg", "Favorite Title"));
        }

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu
        super.onPrepareOptionsMenu(menu);
    }
}
