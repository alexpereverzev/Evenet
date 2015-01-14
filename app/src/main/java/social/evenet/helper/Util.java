package social.evenet.helper;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.animation.AnimatorProxy;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sergey on 09.12.13.
 */
public class Util {
    public static final String PACKAGE = "com.tisoomi.livingmasterclub.";
    public static final String LOGIN_FRAGMENT_TAG = PACKAGE + "tagLoginFragment";
    public static final String IMPULSE_DETAILS_FRAGMENT_TAG = PACKAGE + "tagImpulseDetailsFragment";
    public static final int RESULT_OK = 10;
    public static final int RESULT_EVENT_PLACE = 12;
    public static final int RESULT_OK_WALL = 1000;
    public static final int RESULT_REGISTER_DEVICE = 30;
    public static final int RESULT_COMMENT = 1001;
    public static final int REMOVE = 100;
    public static final int RESULT_ERROR = 11;
    public static final int RESULT_ERROR_REGISTER_DEVICE = 110;

    public static final String BASE_API_URL = "https://test.livingmasterclub.com/rest/";

    public static String convertToDate(String a) {

        DateTime startDate = DateTime.now(); // now() : since Joda Time 2.0
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(a, formatter1);

        Period period = new Period(startDate, endDate, PeriodType.dayTime());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays().appendSuffix(" day ", " days ")
                        //  .appendHours().appendSuffix(" hour ", " hours ")
                        //  .appendMinutes().appendSuffix(" minute ", " minutes ")
                        // .appendSeconds().appendSuffix(" second ", " seconds ")
                .toFormatter();

        return formatter.print(period);
    }

    public static String convertToDateTime(String a) {

        DateTime startDate = DateTime.now(); // now() : since Joda Time 2.0
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(a, formatter1);

        Period period = new Period(startDate, endDate, PeriodType.dayTime());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                //.appendDays().appendSuffix(" day ", " days ")
                .appendHours().appendSuffix(" hour ", " hours ")
                .appendMinutes().appendSuffix(" minute ", " minutes ")
                        // .appendSeconds().appendSuffix(" second ", " seconds ")
                .toFormatter();

        return formatter.print(period);
    }

    public static String checkCacheDB(String a) {

        DateTime startDate = DateTime.now(); // now() : since Joda Time 2.0
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(a, formatter1);

        Period period = new Period(startDate, endDate, PeriodType.dayTime());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                //.appendDays().appendSuffix(" day ", " days ")
                .appendHours().appendSuffix(" hour ", " hours ")
                .appendMinutes().appendSuffix(" minute ", " minutes ")
                        // .appendSeconds().appendSuffix(" second ", " seconds ")
                .toFormatter();

        return formatter.print(period);
    }

    public static boolean convertToYear(String a, DateTime startDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String input = sdf.format(new Date(Long.parseLong(a) * 1000));


        // now() : since Joda Time 2.0
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(input, formatter1);

        Period period = new Period(startDate, endDate, PeriodType.standard());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears().appendSuffix(" year ", " years ,")
                .appendMonths().appendSuffix(" month ", " months ,")

                        //    .appendSeconds().appendSuffix(" second ", " seconds ")
                .toFormatter();
        String s = formatter.print(period);
        if (s.contains("-")) {
            return false;
        } else return false;


    }

    public static String convertToWeek(String a) {
        DateTime startDate = DateTime.now(); // now() : since Joda Time 2.0
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(a, formatter1);

        Period period = new Period(startDate, endDate, PeriodType.standard());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears().appendSuffix(" year ", " years ,")
                .appendMonths().appendSuffix(" month ", " months ,")
                .appendWeeks().appendSuffix(" week ", " weeks ,")
                .appendDays().appendSuffix(" day ", " days ,")
                .appendHours().appendSuffix(" hour ", "h ,")
                .appendMinutes().appendSuffix(" minute ", "m ,")
                        //    .appendSeconds().appendSuffix(" second ", " seconds ")
                .toFormatter();
        String s = formatter.print(period);
        if (s.contains("year")) {
            String[] result = s.split("year");
            s = result[0] + " year ,";
        }
        else if (s.contains("month")){

               s= s.replace("month","months ,");

        }
        else if(s.contains("week"))  s= s.replace("week","weeks ,");


        System.out.println(s);

        return s;

    }

    public static String convertNew(String a) {
        DateTime startDate = DateTime.now(); // now() : since Joda Time 2.0
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(a, formatter1);

        Period period = new Period(startDate, endDate, PeriodType.weeks());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendWeeks().appendSuffix(" week ", " weeks ")

                        //   .appendDays().appendSuffix(" day ", " days ")
                        //   .appendHours().appendSuffix(" hour ", " hours ")
                        //   .appendMinutes().appendSuffix(" minute ", " minutes ")
                        // .appendSeconds().appendSuffix(" second ", " seconds ")
                .toFormatter();

        System.out.println(formatter.print(period));
        String s = formatter.print(period);
        return s;

    }


    public static String convertToMonths(String a) {
        DateTime startDate = DateTime.now(); // now() : since Joda Time 2.0
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(a, formatter1);

        Period period = new Period(startDate, endDate, PeriodType.months());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendMonths()
                .appendSuffix(" month", " months")
                        //   .appendDays().appendSuffix(" day ", " days ")
                        //   .appendHours().appendSuffix(" hour ", " hours ")
                        //   .appendMinutes().appendSuffix(" minute ", " minutes ")
                        // .appendSeconds().appendSuffix(" second ", " seconds ")
                .toFormatter();

        System.out.println(formatter.print(period));
        String s = formatter.print(period);
        return s;

    }


    public static double CalculationByDistance(double initialLat, double initialLong,
                                               double finalLat, double finalLong) {
        int R = 6371; // km

        double dLat = toRadians(finalLat - initialLat);
        double dLon = toRadians(finalLong - initialLong);
        double lat1 = toRadians(initialLat);
        double lat2 = toRadians(finalLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        String str = String.format("%1.2f", R * c);
        str = str.replace(",", ".");
        return Double.valueOf(str);
    }

    public static double toRadians(Double deg) {
        return deg * (Math.PI / 180);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static final int MONDAY_BEFORE_JULIAN_EPOCH = Time.EPOCH_JULIAN_DAY - 3;
    public static final int PULSE_ANIMATOR_DURATION = 544;

    // Alpha level for time picker selection.
    public static final int SELECTED_ALPHA = 51;
    public static final int SELECTED_ALPHA_THEME_DARK = 102;
    // Alpha level for fully opaque.
    public static final int FULL_ALPHA = 255;


    static final String SHARED_PREFS_NAME = "com.android.calendar_preferences";

    public static boolean isJellybeanOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Try to speak the specified text, for accessibility. Only available on JB or later.
     *
     * @param text Text to announce.
     */
    @SuppressLint("NewApi")
    public static void tryAccessibilityAnnounce(View view, CharSequence text) {
        if (isJellybeanOrLater() && view != null && text != null) {
            view.announceForAccessibility(text);
        }
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    /**
     * Takes a number of weeks since the epoch and calculates the Julian day of
     * the Monday for that week.
     * <p/>
     * This assumes that the week containing the {@link android.text.format.Time#EPOCH_JULIAN_DAY}
     * is considered week 0. It returns the Julian day for the Monday
     * {@code week} weeks after the Monday of the week containing the epoch.
     *
     * @param week Number of weeks since the epoch
     * @return The julian day for the Monday of the given week since the epoch
     */
    public static int getJulianMondayFromWeeksSinceEpoch(int week) {
        return MONDAY_BEFORE_JULIAN_EPOCH + week * 7;
    }

    /**
     * Returns the week since {@link android.text.format.Time#EPOCH_JULIAN_DAY} (Jan 1, 1970)
     * adjusted for first day of week.
     * <p/>
     * This takes a julian day and the week start day and calculates which
     * week since {@link android.text.format.Time#EPOCH_JULIAN_DAY} that day occurs in, starting
     * at 0. *Do not* use this to compute the ISO week number for the year.
     *
     * @param julianDay      The julian day to calculate the week number for
     * @param firstDayOfWeek Which week day is the first day of the week,
     *                       see {@link android.text.format.Time#SUNDAY}
     * @return Weeks since the epoch
     */
    public static int getWeeksSinceEpochFromJulianDay(int julianDay, int firstDayOfWeek) {
        int diff = Time.THURSDAY - firstDayOfWeek;
        if (diff < 0) {
            diff += 7;
        }
        int refDay = Time.EPOCH_JULIAN_DAY - diff;
        return (julianDay - refDay) / 7;
    }

    /**
     * Render an animator to pulsate a view in place.
     *
     * @param labelToAnimate the view to pulsate.
     * @return The animator object. Use .start() to begin.
     */
    public static ObjectAnimator getPulseAnimator(View labelToAnimate, float decreaseRatio,
                                                  float increaseRatio) {
        Keyframe k0 = Keyframe.ofFloat(0f, 1f);
        Keyframe k1 = Keyframe.ofFloat(0.275f, decreaseRatio);
        Keyframe k2 = Keyframe.ofFloat(0.69f, increaseRatio);
        Keyframe k3 = Keyframe.ofFloat(1f, 1f);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofKeyframe("scaleX", k0, k1, k2, k3);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofKeyframe("scaleY", k0, k1, k2, k3);
        ObjectAnimator pulseAnimator =
                ObjectAnimator.ofPropertyValuesHolder(
                        AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(labelToAnimate) : labelToAnimate, scaleX,
                        scaleY);
        pulseAnimator.setDuration(PULSE_ANIMATOR_DURATION);

        return pulseAnimator;
    }


}
