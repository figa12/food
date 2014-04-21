package aau.sw8.recipe;

/**
 * Created by Sam on 16/04/2014.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * This class extends ScrollView with a public scroll listener.
 */
public class ObservableScrollView extends ScrollView {

    private ScrollListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }


    public interface ScrollListener {
        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
