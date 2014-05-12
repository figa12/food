package aau.sw8.recipe;

/**
 * Created by Sam on 16/04/2014.
 */
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * This class extends ScrollView with a public scroll listener.
 */
public class ObservableScrollView extends ScrollView {

    private ScrollListener scrollViewListener = null;
    private OnBottomReachedListener onBottomReachedListener = null;

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

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ObservableScrollView.this.wait = false;
        }
    };
    private boolean wait = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
        if (this.onBottomReachedListener != null) {
            View view = getChildAt(getChildCount() - 1);
            int difference = (view.getBottom() - (getHeight() + getScrollY())); //Calculate the scrolldifference

            if (difference == 0 && !this.wait) {
                // Multiple 'bottom reached' events are fired, set flag to filter this out
                this.wait = true;

                // Event on listener
                this.onBottomReachedListener.onBottomReached();

                // Make a runnable that allows 'bottom has been reached'-code to run again in a moment
                this.handler.postDelayed(this.runnable, 100);
            }
        }
    }

    public interface ScrollListener {
        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    public interface OnBottomReachedListener {
        void onBottomReached();
    }
}
