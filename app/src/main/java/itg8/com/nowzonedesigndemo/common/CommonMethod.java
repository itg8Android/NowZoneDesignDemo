package itg8.com.nowzonedesigndemo.common;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Android itg 8 on 4/20/2017.
 */

public class CommonMethod {


    private static Typeface typeface;

    public static Typeface setFontDroidSans(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSans.ttf");
        return typeface;
    }

    public static Typeface setFontDroidSerifRegular(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSerif-Regular.ttf");
        return typeface;
    }

    public static Typeface setFontOpenSansRegular(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        return typeface;
    }public static Typeface setFontOpenSansSemiBold(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
        return typeface;
    }public static Typeface setFontOxygenRegular(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Oxygen-Regular.ttf");
        return typeface;
    }public static Typeface setFontRobotoLight(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/robotolight.ttf");
        return typeface;
    }public static Typeface setJuraMedium(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Jura-Medium.ttf");
        return typeface;
    }

}
