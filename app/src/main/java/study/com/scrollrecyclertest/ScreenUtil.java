package study.com.scrollrecyclertest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ScreenUtil {
    private int appHeight = 0;
    private int appWidth = 0;
    private static int StatusBarHeight;
    private Context context;
    private static ScreenUtil screenUtil;

    public static ScreenUtil getInstance() {
        return screenUtil;
    }

    public static void init(Context context) {
        screenUtil = new ScreenUtil(context);
    }

    public ScreenUtil(Context context) {
        this.context = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int appHeight = dm.heightPixels;
        int appWidth = dm.widthPixels;
        if (appWidth > appHeight) {
            this.appWidth = appHeight;
            this.appHeight = appWidth;
        } else {
            this.appWidth = appWidth;
            this.appHeight = appHeight;
        }

    }

    public int getAppHeight() {
        return this.appHeight;
    }

    public int getAppWidth() {
        return this.appWidth;
    }

    public int px2dip(float pxValue) {
        float scale = this.context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public int dip2px(float dipValue) {
        float scale = this.context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5F);
    }

    public int px2sp(float pxValue) {
        float fontScale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / fontScale + 0.5F);
    }

    public int sp2px(float spValue) {
        float fontScale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }

    public static void setListViewHeightBasedOnMaxHeght(ListView listView, int maxHeight) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            int itemCount = 0;

            for(int i = listAdapter.getCount() - 1; i >= 0; --i) {
                ++itemCount;
                View listItem = listAdapter.getView(i, (View)null, listView);
                listItem.measure(0, 0);
                if (totalHeight + listItem.getMeasuredHeight() > maxHeight) {
                    break;
                }

                totalHeight += listItem.getMeasuredHeight();
            }

            if (itemCount < listAdapter.getCount()) {
                LayoutParams params = listView.getLayoutParams();
                params.height = totalHeight + listView.getDividerHeight() * (itemCount - 1);
                listView.setLayoutParams(params);
            }

        }
    }

    public static int getSysScreenBrightness(Context context) {
        int screenBrightness = 255;

        try {
            screenBrightness = System.getInt(context.getContentResolver(), "screen_brightness");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return screenBrightness;
    }

    public static void setSysScreenBrightness(Context context, int brightness) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = System.getUriFor("screen_brightness");
            System.putInt(resolver, "screen_brightness", brightness);
            resolver.notifyChange(uri, (ContentObserver)null);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static int getScreenBrightnessMode(Context context) {
        int screenMode = 0;

        try {
            screenMode = System.getInt(context.getContentResolver(), "screen_brightness_mode");
        } catch (Exception var3) {
            ;
        }

        return screenMode;
    }

    public static boolean isAutoBrightnessMode(Context context) {
        return getScreenBrightnessMode(context) == 1;
    }

    public static void stopAutoBrightness(Context context) {
        System.putInt(context.getContentResolver(), "screen_brightness_mode", 0);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }

    public static int px2sp(Context context, float pxValue) {
        float scaled = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / scaled + 0.5F);
    }

    public static final int getHeightInPx(Context context) {
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final int getWidthInPx(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }

        return result;
    }

    private static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }

            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (VERSION.SDK_INT >= 19) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String)m.invoke((Object)null, "qemu.hw.mainkeys");
            } catch (Throwable var3) {
                ;
            }
        }

        return sNavBarOverride;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    public static boolean isLandscape(Context context) {
        if (context == null) {
            return false;
        } else {
            return context.getResources().getConfiguration().orientation == 2;
        }
    }

    public static int getStatusBarHeight(Context context) {
        if (StatusBarHeight != 0) {
            return StatusBarHeight;
        } else {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int sbar = 0;

            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                sbar = context.getResources().getDimensionPixelSize(x);
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            StatusBarHeight = sbar;
            return StatusBarHeight;
        }
    }

    public boolean isScreenOriatationPortrait() {
        return this.context.getResources().getConfiguration().orientation == 1;
    }

    public static int getMeasureHeight(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        return view.getMeasuredHeight();
    }

}
