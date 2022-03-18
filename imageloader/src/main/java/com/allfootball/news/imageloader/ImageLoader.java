
package com.allfootball.news.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.allfootball.news.imageloader.glide.GlideImageLoaderStrategy;
import com.allfootball.news.imageloader.progress.OnProgressListener;
import com.bumptech.glide.load.Transformation;

import java.io.File;

/**
 * Created by lihaiyi on 2018/6/14.
 */

public class ImageLoader {

    public final static int SCALE_TYPE_None = -1;

    public final static int SCALE_TYPE_FitCenter = 2;

    public final static int SCALE_TYPE_CenterInside = 5;

    public final static int SCALE_TYPE_CenterCrop = 6;

    private static ImageLoader mInstance;

    private BaseImageStrategy mStrategy;

    public interface STRATEGY_TYPE {
        int GLIDE = 0;
//        int FRESCO = 1;
    }


    // 单例模式，节省资源
    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public ImageLoader() {
        mStrategy = new GlideImageLoaderStrategy();
//        mStrategy = new FrescoImageLoaderStrategy();
    }

    /**
     * 策略模式的注入操作
     *
     * @param strategy
     */
    public void setLoadImgStrategy(BaseImageStrategy strategy) {
        mStrategy = strategy;
    }

    public void init(ImageConfig config) {
        if (config == null) {
            mStrategy = new GlideImageLoaderStrategy();
        } else {
            switch (config.strategyType) {
                case 0:
                default:
                    mStrategy = new GlideImageLoaderStrategy();
            }
        }
        mStrategy.init(config);
    }

    public void loadImage(Context context, ImageOption option) {
        if (context == null)
            return;
        if (mStrategy == null){
            Log.e("ImageLoader","图片库ImageLoad没有初始化");
            return;
        }
        mStrategy.loadImage(context, option);
    }


    public void downloadImage(Context context, String url, ImageListener listener) {
        if (context == null)
            return;
        if (mStrategy == null){
            Log.e("ImageLoader","图片库ImageLoad没有初始化");
            return;
        }
        mStrategy.downloadImage(context, url, listener);
    }

    public void clearCache(Context context) {
        if (context == null)
            return;
        if (mStrategy == null){
            Log.e("ImageLoader","图片库ImageLoad没有初始化");
            return;
        }
        mStrategy.clearCache(context);
    }

    public void clearMemoryCache(Context context) {
        if (context == null)
            return;
        if (mStrategy == null){
            Log.e("ImageLoader","图片库ImageLoad没有初始化");
            return;
        }
        mStrategy.clearMemoryCache(context);
    }

    public File getCachedOnDisk(Context context, String url, ImageLoader.ImageListener listener) {
        if (context == null)
            return null;
        if (mStrategy == null){
            Log.e("ImageLoader","图片库ImageLoad没有初始化");
            return null;
        }
        return mStrategy.getCachedOnDisk(context, url, listener);
    }







    public ImageOption.Builder context(Context context) {
        return new ImageOption.Builder().context(context);
    }

    public ImageOption.Builder url(String url) {
        return new ImageOption.Builder().url(url);
    }

    public ImageOption.Builder placeholder(int placeholder) {
        return new ImageOption.Builder().placeholder(placeholder);
    }
    public ImageOption.Builder resId(int resId) {
        return new ImageOption.Builder().resId(resId);
    }

    public ImageOption.Builder imageView(ImageView imageView) {
        return new ImageOption.Builder().imageView(imageView);
    }

    public ImageOption.Builder roundAsCircle(boolean roundAsCircle) {
        return new ImageOption.Builder().roundAsCircle(roundAsCircle);
    }
    public ImageOption.Builder radiusDp(float radiusDp) {
        return new ImageOption.Builder().radiusDp(radiusDp);
    }

    public ImageOption.Builder scaleType(int scaleType) {
        return new ImageOption.Builder().scaleType(scaleType);
    }

    public ImageOption.Builder autoPlayGif(boolean autoPlayGif) {
        return new ImageOption.Builder().autoPlayGif(autoPlayGif);
    }


    public ImageOption.Builder file(File file) {
        return new ImageOption.Builder().file(file);
    }

    public ImageOption.Builder errorResourceId(int errorResourceId) {
        return new ImageOption.Builder().errorResourceId(errorResourceId);
    }

    public ImageOption.Builder listener(ImageLoader.ImageListener listener) {
        return new ImageOption.Builder().listener(listener);
    }

    public ImageOption.Builder urlArray(String urls[]) {
        return new ImageOption.Builder().urlArray(urls);
    }
    public ImageOption.Builder timeOutMillisecond(int timeOutMillisecond) {
        return new ImageOption.Builder().timeOutMillisecond(timeOutMillisecond);
    }
    public ImageOption.Builder setOnProgressListener(OnProgressListener onProgressListener) {
        return new ImageOption.Builder().setOnProgressListener(onProgressListener);
    }
    public ImageOption.Builder cornerType(int  cornerType) {
        return new ImageOption.Builder().cornerType(cornerType);
    }
    public ImageOption.Builder roundingBorderWidth(int  roundingBorderWidth) {
        return new ImageOption.Builder().roundingBorderWidth(roundingBorderWidth);
    }
    public ImageOption.Builder roundingBorderColor(int  roundingBorderColor) {
        return new ImageOption.Builder().roundingBorderColor(roundingBorderColor);
    }
    public ImageOption.Builder transformation(Transformation<Bitmap>  transformation) {
        return new ImageOption.Builder().transformation(transformation);
    }
    public ImageOption.Builder drawable(Drawable drawable) {
        return new ImageOption.Builder().drawable(drawable);
    }


    public static interface BaseImageListener {


        void onSuccess(Drawable drawable, boolean isGif);

        void onFail();

        void onDownloaded(String filePath, String reNameFilepath);
    }

    public static class ImageListener implements BaseImageListener {

        public boolean resourceReady;

        @Override
        public void onSuccess(Drawable drawable, boolean isGif) {
            resourceReady = true;
        }

        @Override
        public void onFail() {
            resourceReady = false;
        }

        @Override
        public void onDownloaded(String filePath, String reNameFilepath) {
            resourceReady = true;
        }

    }


}
