package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aau.sw8.model.Ingredient;

/**
 * Created by jeria_000 on 0010 10. apr.
 */
public class IngredientButton extends LinearLayout {

    public interface IngredientButtonClickListener {
        void OnSelectedChanged(boolean isSelected);
        void OnHighlightedChanged(boolean isHighlighted);
    }

    private IngredientButtonClickListener ingredientButtonClickListener;
    private Ingredient ingredient;

    private TextView nameTextView;
    //private LinearLayout layoutRoot;
    private LinearLayout removeLinearLayout;

    private boolean isHighlighted = false;

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

        this.removeLinearLayout = (LinearLayout) ingredientView.findViewById(R.id.ingredientRemoveLayout);

        ImageButton unselectImageButton = (ImageButton) ingredientView.findViewById(R.id.ingredientRemoveButton);
        unselectImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IngredientButton.super.isSelected()) {
                    IngredientButton.this.setSelected(false);
                }
            }
        });

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientButton.this.onClick();
            }
        });

        this.nameTextView = (TextView) ingredientView.findViewById(R.id.ingredientName);
    }

    private void onClick() {
        if (!IngredientButton.super.isSelected()) {
            IngredientButton.this.setSelected(true);
        } else {
            IngredientButton.this.setHighlighted(!IngredientButton.this.isHighlighted());
        }
    }

    @Override
    public void setSelected(boolean selected) {
        boolean oldValue = this.isSelected();
        super.setSelected(selected);

        if (selected) {
            this.removeLinearLayout.setVisibility(VISIBLE);
            super.refreshDrawableState();
        } else {
            this.setHighlighted(false); // calls refreshDrawableState()
            this.removeLinearLayout.setVisibility(GONE);
        }

        if (oldValue != selected && this.ingredientButtonClickListener != null) {
            this.ingredientButtonClickListener.OnSelectedChanged(this.isSelected());
        }
    }

    public boolean isHighlighted() {
        return this.isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        boolean oldValue = this.isHighlighted;

        if (isHighlighted) {
            if (this.isSelected()) {
                // only allow highlight if selected
                this.isHighlighted = isHighlighted;
            }
        } else {
            this.isHighlighted = isHighlighted;
        }

        if (oldValue != isHighlighted && this.ingredientButtonClickListener != null) {
            this.ingredientButtonClickListener.OnHighlightedChanged(this.isHighlighted);
        }

        super.refreshDrawableState();
    }

    private static final int[] STATE_HIGHLIGHTED = { R.attr.highlighted };
    private static final int[] STATE_SELECTED = { R.attr.selected };

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);

        if (this.isSelected()) {
            mergeDrawableStates(drawableState, IngredientButton.STATE_SELECTED);
        }
        if (this.isHighlighted) {
            mergeDrawableStates(drawableState, IngredientButton.STATE_HIGHLIGHTED);
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
}
