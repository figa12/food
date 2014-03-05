package aau.sw8.recipe;

import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.junit.Test;

import org.robolectric.RobolectricTestRunner;
import org.w3c.dom.Text;

import android.app.Activity;
import android.widget.TextView;

import dalvik.annotation.TestTargetClass;

@RunWith(RobolectricTestRunner.class)
public class FirstTest {
    @Test
    public void testInstantiation() {
        Actvity actvity = new Activity();
        TextView tv = TextView(activity);
        tv.setText("hest");

        assertEquals("hest", tv.getText());
    }
}