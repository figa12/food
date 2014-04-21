package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jeria_000 on 07-03-14.
 */
public class ShoppingListFragment extends Fragment {

    private String pageTitle; // Not really needed, but saved just in case

    /* Constructors */
    public ShoppingListFragment() {
        // Required empty public constructor (?)
    }

    /* Override methods */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        // TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        //TODO handle other views here

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
