package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Jesper on 17-09-13.
 * An abstract class for making a {@link android.widget.LinearLayout} that works almost the same way as a {@link android.widget.ListView}.
 *
 * When inheriting the class you must specify an object which is the data representation of each view.
 * Classes must override the abstract method {@code makeView(ListObject object)} which should create a view representation of the given {@link ListObject}.
 */
public abstract class ListLinearLayout<ListObject> extends LinearLayout {

    /** List of {@link ListObject} */
    protected ArrayList<ListObject> items = new ArrayList<ListObject>();

    /** If set to true, the next addView(View) is allowed */
    private boolean allowedAddView = true;

    public ListLinearLayout(Context context) {
        super(context);
        super.setOrientation(LinearLayout.VERTICAL);
    }

    public ListLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param index Index of a {@link ListObject}.
     * @return The {@link ListObject} at the specified index.
     */
    public ListObject get(int index) {
        return this.items.get(index);
    }

    /** @return The number of elements in this {@link aau.sw8.recipe.ListLinearLayout}.*/
    public int getSize() {
        return this.items.size();
    }

    /**
     * Creates a view for the specified {@link ListObject}
     * @param object The object to add.
     * @return A {@link android.view.View} representation of the data.
     */
    protected abstract View makeView(ListObject object);

    /**
     * Add the specified {@link ListObject} at the bottom of this {@link aau.sw8.recipe.ListLinearLayout}.
     * @param listObject The object to add.
     */
    public final void addViewAtBottom(ListObject listObject) {
        this.addView(listObject);
    }

    /**
     * Add the specified {@link ListObject} to this {@link aau.sw8.recipe.ListLinearLayout}.
     * @param listObject
     */
    public final void addView(ListObject listObject) {
        this.items.add(listObject);

        this.allowedAddView = true;
        super.addView(this.makeView(listObject));
    }

    /**
     * Add the specified {@link ListObject} at the top of this {@link aau.sw8.recipe.ListLinearLayout}.
     * @param listObject The object to add.
     */
    public final void addViewAtTop(ListObject listObject) {
        //Add at index 0 in both
        this.items.add(0, listObject);

        this.allowedAddView = true;
        super.addView(this.makeView(listObject), 0);
    }

    /**
     * Remove a {@link ListObject} at the specified index.
     * @param index The index in the {@link aau.sw8.recipe.ListLinearLayout} of the {@link ListObject}.
     */
    @Override
    public final void removeViewAt(int index) {
        // Remove from both lists
        super.removeViewAt(index);
        this.items.remove(index);
    }

    /**
     * Searches the {@link aau.sw8.recipe.ListLinearLayout} for the {@link ListObject} and removes it.
     * @param listObject The {@link ListObject} to remove.
     */
    public final void removeView(ListObject listObject) {
        // I think this will give an exception if the items doesn't exist. But you should not try to remove items that doesn't exist.
        this.removeViewAt(this.items.indexOf(listObject));
    }

    @Override
    @Deprecated
    public final void addView(View view) {
        // if the call to addView(View) came from within this class, it is allowed
        if (this.allowedAddView) {
            super.addView(view);
            this.allowedAddView = false;
        } else {
            throw new RuntimeException("addView(View) is not allowed on a ListLinearLayout implementation");
        }
    }

    @Override
    @Deprecated
    public final void addView(View view, int index) {
        // if the call to addView(View) came from within this class, it is allowed
        if (this.allowedAddView) {
            super.addView(view, index);
            this.allowedAddView = false;
        } else {
            throw new RuntimeException("addView(View) is not allowed on a ListLinearLayout implementation");
        }
    }

    /**
     * Please don't remove views by reference, remove with {@link ListObject} instead
     * @param view to be removed
     */
    @Override
    @Deprecated
    public final void removeView(View view) {
        int index = super.indexOfChild(view);
        this.removeViewAt(index);
        this.items.remove(index);
    }

    @Override
    public final void removeAllViews() {
        super.removeAllViews();
        this.items.clear();
    }

    @Override
    public final void removeAllViewsInLayout() {
        this.removeAllViews();
    }

    @Override
    public final void removeViews(int start, int count) {
        super.removeViews(start, count);
        for (int i = 0; i < count; i++) {
            // continuously remove at the start index, because the indices changes
            this.items.remove(start);
        }
    }

    /**
     * Please don't remove views by reference, remove with {@link ListObject} instead
     * @param view to be removed
     */
    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public final void removeViewInLayout(View view) {
        this.removeView(view);
    }

    @Override
    public final void removeViewsInLayout(int start, int count) {
        this.removeViews(start, count);
    }
}
