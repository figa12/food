package aau.sw8.recipe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

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
        this.pageTitle = super.getResources().getString(R.string.sign_in);
        super.getActivity().setTitle(this.pageTitle);

        //TODO handle other views here
        SignInButton signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        Button signOutButton = (Button) rootView.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(this);

        Button revokeAccesButton = (Button) rootView.findViewById(R.id.revoke_access_button);
        revokeAccesButton.setOnClickListener(this);
        if(BaseActivity.user == null){
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
            revokeAccesButton.setVisibility(View.GONE);
        }else{
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            revokeAccesButton.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public void onClick(View view) {
        if(view.getId() == R.id.sign_in_button){
            ((MainActivity)getActivity()).googlePlusActions(BaseActivity.SIGN_IN);
            ((MainActivity)getActivity()).selectItem(0);
        }else if(view.getId() == R.id.sign_out_button){
            ((MainActivity)getActivity()).googlePlusActions(BaseActivity.SIGN_OUT);
        }else if(view.getId() == R.id.revoke_access_button){
            ((MainActivity)getActivity()).googlePlusActions(BaseActivity.REWOKE_ACCESS);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //MainActivity mainActivity = (MainActivity) this.getActivity();
        // use mainActivity.isDrawerOpen() to handle fragment specific menu
        super.onPrepareOptionsMenu(menu);
    }
}
