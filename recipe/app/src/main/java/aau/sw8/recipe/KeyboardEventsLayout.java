package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class KeyboardEventsLayout extends FrameLayout {

    public interface OnKeyboardVisibilityChangedListener {
        void OnVisibilityChanged(boolean isVisible);
    }

    private boolean viewCreated = false;

    private OnKeyboardVisibilityChangedListener onKeyboardVisibilityChangedListener;
    private boolean isKeyboardVisible = false;

    public KeyboardEventsLayout(Context context) {
        super(context);
    }

    public KeyboardEventsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnKeyboardVisibilityChangedListener(OnKeyboardVisibilityChangedListener onKeyboardVisibilityChangedListener) {
        this.onKeyboardVisibilityChangedListener = onKeyboardVisibilityChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = super.getHeight();

        if (this.viewCreated) {
            // if actualheight > proposedHeight, then the keyboard is visible
            this.setKeyboardVisible(actualHeight >= proposedheight);
        } else {
            if (actualHeight == proposedheight) {
                this.viewCreated = true;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean isKeyboardVisible() {
        return this.isKeyboardVisible;
    }

    private void setKeyboardVisible(boolean isKeyboardVisible) {
        boolean oldValue = this.isKeyboardVisible;
        this.isKeyboardVisible = isKeyboardVisible;

        // if isKeyboardVisible is changing, then fire event
        if (isKeyboardVisible != oldValue) {
            this.onKeyboardVisiblityChanged();
        }
    }

    private void onKeyboardVisiblityChanged() {
        if (this.onKeyboardVisibilityChangedListener != null) {
            this.onKeyboardVisibilityChangedListener.OnVisibilityChanged(this.isKeyboardVisible);
        }
    }
}
