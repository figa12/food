package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import aau.sw8.model.InstructionStep;

/**
 * Created by Jesper on 07-03-14.
 */
public class InstructionList extends ListLinearLayout<InstructionStep> {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    private ArrayList<TextView> textViews = new ArrayList<TextView>();

    public InstructionList(Context context) {
        super(context);
    }

    public InstructionList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected View makeView(InstructionStep instruction) {
        // Get inflater and inflate item
        LayoutInflater layoutInflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View instructionView = layoutInflater.inflate(R.layout.instruction_step_item, null);

        // set step number
        TextView step = (TextView) instructionView.findViewById(R.id.stepNumberTextView);
        step.setText(String.valueOf(instruction.getStep()) + ". ");

        if (instruction.getImagePath() != null) {
            // set step image
            ImageView recipeImage = (ImageView) instructionView.findViewById(R.id.stepDescriptionImageView);
            recipeImage.setVisibility(VISIBLE);
            this.imageLoader.displayImage(instruction.getImagePath(), recipeImage, this.imageLoaderOptions);
        }

        // set step description
        TextView description = (TextView) instructionView.findViewById(R.id.stepDescriptionTextView);
        description.setText(instruction.getDescription());

        this.textViews.add(step);
        this.textViews.add(description);

        return instructionView;
    }

    public void setFontSize(float value) {
        for (int i = 0; i < this.textViews.size(); i++) {
            this.textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, value);
        }
    }
}
