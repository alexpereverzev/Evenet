package social.evenet.pager;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Александр on 30.11.2014.
 */
public class Util {

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
}
