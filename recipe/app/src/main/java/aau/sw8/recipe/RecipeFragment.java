package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jesper on 12-03-14.
 */
public class RecipeFragment extends Fragment {

    public static final String ARG_RECIPE = "recipe";

    private Recipe recipe;

    private String pageTitle;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);
        this.recipe = super.getArguments().getParcelable(RecipeFragment.ARG_RECIPE);

        this.pageTitle = super.getResources().getString(R.string.fragment_recipe);
        super.getActivity().setTitle(this.pageTitle);

        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu
        super.onPrepareOptionsMenu(menu);
    }


}
