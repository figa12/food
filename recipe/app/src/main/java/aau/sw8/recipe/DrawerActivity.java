package aau.sw8.recipe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Sam on 14/04/2014.
 */
public abstract class DrawerActivity extends LogInActivity implements RecipeSearchFragment.OnFragmentInteractionListener {

    private Settings settings;
    protected CharSequence drawerTitle;
    public static int CHOSEN_FRAGMENT;
    protected CharSequence title;
    private String[] pageTitles;
    protected DrawerLayout drawerLayout;
    protected LinearLayout drawerLinearLayout;
    protected ListView drawerListView;
    protected ActionBarDrawerToggle drawerToggle;
    protected static final int FRAGMENT_CHANGE_REQUEST = 11; // can be any number
    private int actionBarHeight;
    private TypedValue typedValue = new TypedValue();
    protected TextView drawerSignInBtn;


    private static final String TAG = "DrawerActivity";

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.drawer_layout); // "super" is used because "this" is overridden

        setupDrawer();

        this.settings = new Settings(this);

        // open the navigation drawer the first time the application opens
        if (!this.settings.isNavigationDrawerDisplayed()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }, 200);
            this.settings.setNavigationDrawerDisplayed(true);
            this.settings.saveSettings();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupDrawer() {
        this.title = DrawerActivity.this.drawerTitle = super.getTitle();
        this.pageTitles = super.getResources().getStringArray(R.array.pages_array);
        this.drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        this.drawerLinearLayout = (LinearLayout) super.findViewById(R.id.left_drawer);
        this.drawerListView = (ListView) super.findViewById(R.id.left_menu);

        /*Sign in button in the bottom of the navigation drawer*/
        this.drawerSignInBtn = (TextView) super.findViewById(R.id.btn_sign_in_drawer);
        this.drawerSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView) view).getText().equals(getResources().getString(R.string.sign_in))) {
                    if(LogInActivity.IS_ONLY_GOOGLE_PLUS){
                        googlePlusLogInActions(LogInActivity.SIGN_IN);
                    }else{
                        DrawerActivity.this.selectItem(99); //starts the Sign in fragment
                    }
                } else {
                    googlePlusLogInActions(LogInActivity.SIGN_OUT); //sign out
                }
            }
        });

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        this.drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, this.pageTitles));
        this.drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerActivity.this.selectItem(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        super.getActionBar().setDisplayHomeAsUpEnabled(true);
        super.getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon
        this.drawerToggle = new ActionBarDrawerToggle(
                this,                               /* host Activity */
                this.drawerLayout,          /* DrawerLayout object */
                R.drawable.ic_drawer,               /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,               /* "open drawer" description for accessibility */
                R.string.drawer_close               /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                DrawerActivity.super.getActionBar().setTitle(DrawerActivity.this.title);
                DrawerActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                DrawerActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                DrawerActivity.this.dismissKeyboard();
                DrawerActivity.super.getActionBar().setTitle(DrawerActivity.this.drawerTitle);
                DrawerActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                DrawerActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        this.drawerLayout.setDrawerListener(this.drawerToggle);
        super.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                DrawerActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
            }
        });
    }

    /***
     * Updated the Navigation drawer UI
     * @param IsSignIn
     */
    @Override
    public void updateUserUI(boolean IsSignIn){
        if(IsSignIn){
            this.drawerSignInBtn.setText(R.string.sign_out);
        }else{
            this.drawerSignInBtn.setText(R.string.sign_in);
        }
    }

    /**
     * Called when an activity that has been started using "startActivityForResult" returns.
     * Allows recipe activities to request fragment change.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "ActivityResult: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

         if(requestCode == DrawerActivity.FRAGMENT_CHANGE_REQUEST) {
             if (resultCode == RESULT_OK) { // child activity is requesting a fragment change
                 // Get requested fragment id
                 int position = data.getIntExtra("position", 0);

                 // Change fragment
                 this.selectItem(position);
             }
         }
    }

    /**
     * Overridden to inflate the drawer layout instead of actually setting the content view
     * */
    @Override
    public void setContentView(final int layoutResID) {
        FrameLayout content = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, content, true);
        // here you can get your drawer buttons and define how they should behave and what must they do, so you won't be needing to repeat it in every activity class
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        super.getActionBar().setTitle(this.title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open/close the drawer or act as back button
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (this.isDrawerOpen()) {
            this.drawerLayout.closeDrawers();
            return true;
        } else if (item.getItemId() == android.R.id.home && super.getFragmentManager().popBackStackImmediate()) {
            return true;
        }

        // Handle action buttons
        switch(item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    /***
     * Return the state of the drawer.
     * @return isDrawerOpen
     */
    public boolean isDrawerOpen() {
        return this.drawerLayout.isDrawerOpen(this.drawerLinearLayout);
    }

    @SuppressWarnings("ConstantConditions")
    protected void selectItem(int position) {
        if (this.isTaskRoot()) { // Replace fragment if this is the main activity
            Fragment fragment;
            Bundle args = new Bundle();

            // Insert cases for other fragments here
            // the indexes corresponds to the order their page names are defined in strings.xml
            switch (position) {
                case 0:
                    fragment = new IngredientSearchFragment();
                    break;

                case 1:
                    fragment = new RecipeSearchFragment();
                    break;

                case 2:
                    if (LogInActivity.user != null){
                        fragment = new FavouriteFragment();
                    }else{
                        fragment = new SignInFragment();
                    }
                    break;

                case 99:
                    fragment = new SignInFragment();
                    break;

                default:
                    Toast.makeText(this, "Fragment not implemented", Toast.LENGTH_LONG).show();
                    return;
            }

            // Enable fragments to handle the action bar
            fragment.setHasOptionsMenu(false);

            // Clear the back stack
            while (super.getFragmentManager().popBackStackImmediate()) ;

            // give the fragment its position as argument
            args.putInt(MainActivity.ARG_POSITION, position);
            fragment.setArguments(args);

            // update selected item and title, then close the drawer
            this.drawerListView.setItemChecked(position, true);
            if(fragment instanceof SignInFragment){
                this.setTitle(R.string.sign_in);
            }else{
                this.setTitle(this.pageTitles[position]);
            }
            this.drawerLayout.closeDrawers();

            // replace the current view with the fragment
            super.getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        else {
            // close this activity and send a request to the parent activity to replace the fragment
            Intent intent = new Intent();
            intent.putExtra("position", position);
            this.setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    protected void setActionBarArrowDependingOnFragmentsBackStack() {
        int backStackEntryCount = super.getFragmentManager().getBackStackEntryCount();
        boolean shouldEnableDrawerIndicator = (this.isTaskRoot() && backStackEntryCount == 0);
        this.drawerToggle.setDrawerIndicatorEnabled(shouldEnableDrawerIndicator);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO What does this do?!?!
        //mayhaps: A method that handles interactions from the current underlying Fragment
    }

    @SuppressWarnings("ConstantConditions")
    public int getActionBarHeight() {
        if (actionBarHeight != 0) {
            return actionBarHeight;
        }
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        return actionBarHeight;
    }

    protected void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(super.findViewById(R.id.content_frame).getWindowToken(), 0);
    }
}