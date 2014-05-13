package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import aau.sw8.model.Ingredient;

/**
 * Created by jeria_000 on 0010 10. apr.
 */
public class IngredientButton extends LinearLayout {

    public interface IngredientButtonClickListener {
        void OnSelectedChanged(boolean isSelected);
    }

    private IngredientButtonClickListener ingredientButtonClickListener;
    private Ingredient ingredient;

    private TextView nameTextView;

    private boolean isSelected = false;

    public IngredientButton(Context context) {
        super(context);
        this.construct();
    }

    public IngredientButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.construct();
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.nameTextView.setText(this.ingredient.getSingular());
    }

    public void setIngredientButtonClickListener(IngredientButtonClickListener ingredientButtonClickListener) {
        this.ingredientButtonClickListener = ingredientButtonClickListener;
    }

    @SuppressWarnings("ConstantConditions")
    private void construct() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        super.setLayoutParams(layoutParams);
        super.setOrientation(HORIZONTAL);
        super.setBackgroundResource(R.drawable.button_ingredient);

        super.setClickable(true);

        LayoutInflater layoutInflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ingredientView = layoutInflater.inflate(R.layout.ingredient_flow_item, null);
        super.addView(ingredientView);

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientButton.this.onClick();
            }
        });

        this.nameTextView = (TextView) ingredientView.findViewById(R.id.ingredientName);
    }

    private void onClick() {
        IngredientButton.this.setSelected(!IngredientButton.this.isSelected());
    }

    @Override
    public boolean isSelected() {
        return this.isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        boolean oldValue = this.isSelected;

        this.isSelected = isSelected;

        if (oldValue != isSelected && this.ingredientButtonClickListener != null) {
            this.ingredientButtonClickListener.OnSelectedChanged(this.isSelected);
        }

        super.refreshDrawableState();
    }

    private static final int[] STATE_SELECTED = { R.attr.selected };

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);

        if (this.isSelected) {
            mergeDrawableStates(drawableState, IngredientButton.STATE_SELECTED);
        }

        return drawableState;
    }

    public void setText(String name) {
        this.nameTextView.setText(name);
    }

    @SuppressWarnings("ConstantConditions")
    public String getText() {
        return this.nameTextView.getText().toString();
    }


    public boolean equals(IngredientButton ingredientButton){
        if(this.getIngredient().getId() == ingredientButton.getIngredient().getId()){
            return true;
        }
        else
            return false;
    }

}
