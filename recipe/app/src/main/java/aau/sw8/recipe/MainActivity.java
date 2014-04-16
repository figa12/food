package aau.sw8.recipe;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.message.BasicNameValuePair;

import aau.sw8.data.ServerCom;
import aau.sw8.model.Recipe;
import aau.sw8.model.User;

public class MainActivity extends Activity implements SearchFragment.OnFragmentInteractionListener, ShoppingListFragment.OnFragmentInteractionListener, RecipeSearchFragment.OnFragmentInteractionListener {

    /*Variables*/
    public static final String ARG_POSITION = "position";

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;

    private User user;

    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] pageTitles;

    public ServerCom serverCom;

    /*Override methods*/
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Image loader*/
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        /*Server communication*/
        serverCom = ServerCom.getInstance();
        serverCom.init(this);

        /* ServerCom testing //TODO should be removed at some point
        serverCom.startNewTask(new BasicNameValuePair("Request", "1"), new BasicNameValuePair("Data", "test1"));
        serverCom.startNewTask(new BasicNameValuePair("Request", "2"), new BasicNameValuePair("Data", "test2"));
        */

        /*Check the phone for sign-in tokens!*/
        //user = new User(1,1234,"KoenBjarne");
        user = null;

        this.title = this.drawerTitle = super.getTitle();
        this.pageTitles = super.getResources().getStringArray(R.array.pages_array);
        this.drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        this.drawerListView = (ListView) super.findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        this.drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, this.pageTitles));
        this.drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.selectItem(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        super.getActionBar().setDisplayHomeAsUpEnabled(true);
        super.getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon
        this.drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                this.drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                MainActivity.super.getActionBar().setTitle(MainActivity.this.title);
                MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                MainActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                MainActivity.super.getActionBar().setTitle(MainActivity.this.drawerTitle);
                MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                MainActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        this.drawerLayout.setDrawerListener(this.drawerToggle);
        super.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
            }
        });

        if (savedInstanceState == null) {
            this.selectItem(0);
        } else {
            MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
        }

        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        //TODO Implement what happens when the user clicks a suggestion
        /* Also need to find out how to give different suggestions for ingredients and recipes */
        
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
         //   Intent wordIntent = new Intent(this, WordActivity.class);
           // wordIntent.setData(intent.getData());
           // startActivity(wordIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //showResults(query);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void selectItem(int position) {

        Fragment fragment = null; // update the main content by replacing fragments
        Bundle args = new Bundle();

        // Insert cases for other fragments here
        // the indexes corresponds to the order their page names are defined in strings.xml
        switch (position)
        {
            case 0:
                fragment = new SearchFragment();
                break;

            case 1:
                fragment = new RecipeSearchFragment();
                break;

            case 2:
                fragment = new FavouriteFragment();
                break;

            case 3:
                fragment = new ShoppingListFragment();
                break;

            default:
                Toast.makeText(this, "Fragment not implemented", Toast.LENGTH_LONG).show();
                return;
        }

        // Enable fragments to handle the action bar
        fragment.setHasOptionsMenu(true);

        while (super.getFragmentManager().popBackStackImmediate());

        // give the fragment its position as argument
        args.putInt(MainActivity.ARG_POSITION, position);
        fragment.setArguments(args);

        // replace the current view with the fragment
        super.getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        this.drawerListView.setItemChecked(position, true);
        this.setTitle(this.pageTitles[position]);
        this.drawerLayout.closeDrawer(this.drawerListView);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        super.getActionBar().setTitle(this.title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchBar = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchBar.setIconifiedByDefault(false);
        searchBar.setQueryHint("Enter a recipe name");

        return true;
    }



    /* Called whenever we call invalidateOptionsMenu() */
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = this.isDrawerOpen();

        /*Hide all the menu items, and show when entering the correct fragment*/
        menu.findItem(R.id.favourite_button).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
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
            case R.id.action_search:
                onSearchRequested();
                return true;
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO What does this do?!?!
        //mayhaps: A method that handles interactions from the current underlying Fragment
    }

    /*Class methods*/
    public User getUser() {
        return user;
    }

    private void setActionBarArrowDependingOnFragmentsBackStack() {
        int backStackEntryCount = super.getFragmentManager().getBackStackEntryCount();
        boolean shouldEnableDrawerIndicator = backStackEntryCount == 0;
        this.drawerToggle.setDrawerIndicatorEnabled(shouldEnableDrawerIndicator);
    }

    public boolean isDrawerOpen() {
        return this.drawerLayout.isDrawerOpen(this.drawerListView);
    }

    public void onServerTest(String result){
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("ConstantConditions")
    public void openRecipeFragment(Recipe recipe) {
        Fragment recipeFragment = new RecipeFragment();
        recipeFragment.setHasOptionsMenu(true);

        Bundle args = new Bundle();
        args.putParcelable(RecipeFragment.ARG_RECIPE, recipe);
        recipeFragment.setArguments(args);

        // replace the current view with the fragment
        super.getFragmentManager().beginTransaction().replace(R.id.content_frame, recipeFragment).addToBackStack(this.title.toString()).commit();

        // update selected item and title, then close the drawer
        this.drawerListView.setItemChecked(this.drawerListView.getSelectedItemPosition(), false);
    }
}
