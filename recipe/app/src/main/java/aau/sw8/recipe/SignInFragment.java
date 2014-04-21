package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import aau.sw8.model.Recipe;
import aau.sw8.model.User;

/**
 * Created by jacob on 4/21/14.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {
    String pageTitle;
    private User currentUser;

    /* Contstructors */
    public SignInFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        int pageIndex = super.getArguments().getInt(MainActivity.ARG_POSITION);

        // TODO: make it possible to change the pagetitle accordingly to language.
        this.pageTitle = super.getResources().getStringArray(R.array.pages_array)[pageIndex];
        super.getActivity().setTitle(this.pageTitle);

        //TODO handle other views here
        rootView.findViewById(R.id.btn_sign_in).setOnClickListener(this);

        return rootView;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button)
            ((MainActivity)getActivity()).SignIn();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu
        super.onPrepareOptionsMenu(menu);
    }
}