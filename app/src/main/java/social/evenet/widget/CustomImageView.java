package social.evenet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Alexander on 07.11.2014.
 */
public class CustomImageView extends ImageView
{
    public CustomImageView (Context context, AttributeSet attributeset, int int_style)
    {
        super(context, attributeset, int_style);
    }

    public CustomImageView (Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public CustomImageView (Context context)
    {
        super(context);
    }

    @Override
    public void requestLayout()
    {
        // Do nothing here
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


}