package aau.sw8.recipe.test;

import android.test.ActivityInstrumentationTestCase2;
import aau.sw8.recipe.MainActivity;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.mainActivity = this.getActivity();
    }

    public void testBla() throws Exception {
        assertTrue("Does MainActivty exist test", this.mainActivity != null);
    }
}
