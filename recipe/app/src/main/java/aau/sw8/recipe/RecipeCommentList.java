package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jesper on 07-03-14.
 */
public class RecipeCommentList extends ListLinearLayout<Comment> {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    public RecipeCommentList(Context context) {
        super(context);
    }

    public RecipeCommentList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected View makeView(Comment comment) {
        // Get inflater and inflate item
        LayoutInflater layoutInflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View commentView = layoutInflater.inflate(R.layout.comment_list_item, null);

        ImageView commentImage = (ImageView) commentView.findViewById(R.id.commentImageView);
        this.imageLoader.displayImage("http://figz.dk/images/microsfot.jpeg", commentImage, this.imageLoaderOptions);

        TextView authorTextView = (TextView) commentView.findViewById(R.id.commentAuthorTextView);
        authorTextView.setText(authorTextView.getText() + " " + comment.getUser().getUsername());

        TextView commentTextView = (TextView) commentView.findViewById(R.id.commentText);
        commentTextView.setText(comment.getText());

        return commentView;
    }
}
