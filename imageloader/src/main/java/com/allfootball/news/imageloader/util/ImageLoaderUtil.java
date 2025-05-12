package com.allfootball.news.imageloader.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by baofu on 2018/6/26.
 */

public class ImageLoaderUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public static String getDownloadPath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return context.getFilesDir().getAbsolutePath();
        }
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (file != null) {
            return file.getAbsolutePath();
        }
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return context.getFilesDir().getAbsolutePath();
    }
}
