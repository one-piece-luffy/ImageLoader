
package com.allfootball.news.imageloader.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.allfootball.news.imageloader.glide.GlideImageLoaderStrategy;
import com.allfootball.news.imageloader.progress.OnProgressListener;

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

    private BaseImageLoaderStrategy mStrategy;

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
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }

    public void init(){
        mStrategy.init();

    }

    public void loadImage(Context context, String url, int placeholder, int failRes,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
        if (context == null)
            return;
        mStrategy.loadImage(context, url, placeholder, failRes, imageView, roundAsCircle,
                roundedCornerRadius, scaleType, autoPlayGif);
    }

    public void loadImage(Context context, Uri uri, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
        if (context == null)
            return;
        mStrategy.loadImage(context, uri, placeholder, errorResourceId, imageView, roundAsCircle,
                roundedCornerRadius, scaleType, autoPlayGif);
    }

    public void loadImage(Context context, File file, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
        if (context == null)
            return;
        mStrategy.loadImage(context, file, placeholder, errorResourceId, imageView, roundAsCircle,
                roundedCornerRadius, scaleType, autoPlayGif);
    }

    public void loadImage(Context context, int resourceId, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif) {
        if (context == null)
            return;
        mStrategy.loadImage(context, resourceId, placeholder, errorResourceId, imageView,
                roundAsCircle, roundedCornerRadius, scaleType, autoPlayGif);

    }

    public void loadImage(Context context, String url,ImageListener listener){
        if (context == null)
            return;
        mStrategy.loadImage(context,url,listener);
    }

    public  void loadImage(Context context, String url, ImageView imageView, boolean autoPlayGif,
                           ImageLoader.ImageListener listener){
        if (context == null)
            return;
        mStrategy.loadImage(context,url,imageView,autoPlayGif,listener);
    }

    public  void loadImage(Context context, String url, ImageView imageView, boolean autoPlayGif,
                           ImageLoader.ImageListener listener, OnProgressListener onProgressListener){
        if (context == null)
            return;
        mStrategy.loadImage(context,url,imageView,autoPlayGif,listener,onProgressListener);
    }

    public void loadImage(Context context, String url, int placeholder, int failRes
            , ImageView imageView, boolean roundAsCircle, float roundedCornerRadius
            , int scaleType,int cornerType, boolean autoPlayGif) {
        if (context == null)
            return;
        mStrategy.loadImage(context, url, placeholder, failRes, imageView, roundAsCircle,
                roundedCornerRadius, scaleType, cornerType, autoPlayGif);
    }

    public void loadImage(Context context, Uri uri, int placeholder, int failRes
            , ImageView imageView, boolean roundAsCircle, float roundedCornerRadius
            , int scaleType,int cornerType, boolean autoPlayGif) {
        if (context == null)
            return;
        mStrategy.loadImage(context, uri, placeholder, failRes, imageView, roundAsCircle,
                roundedCornerRadius, scaleType, cornerType, autoPlayGif);
    }

    public void loadImage(Context context, int resourceId, int placeholder, int failRes
            , ImageView imageView, boolean roundAsCircle, float roundedCornerRadius
            , int scaleType,int cornerType, boolean autoPlayGif) {
        if (context == null)
            return;
        mStrategy.loadImage(context, resourceId, placeholder, failRes, imageView, roundAsCircle,
                roundedCornerRadius, scaleType, cornerType, autoPlayGif);
    }

    public void loadImage(Context context
            , ImageView imageView, boolean autoPlayGif, final ImageLoader.ImageListener listener,String ... url) {
        if (context == null)
            return;
        mStrategy.loadImage(context, imageView,  autoPlayGif, listener, url);
    }

    public void loadImage(Context context
            , ImageView imageView, boolean autoPlayGif, int timeout, String ... url) {
        if (context == null)
            return;
        mStrategy.loadImage(context, imageView,  autoPlayGif, timeout, url);
    }

    public void downloadImage(Context context, String url,ImageListener listener){
        if (context == null)
            return;
        mStrategy.downloadImage(context,url,listener);
    }

    public void clearCache(Context context){
        if (context == null)
            return;
        mStrategy.clearCache(context);
    }

    public  void clearMemoryCache(Context context){
        if (context == null)
            return;
        mStrategy.clearMemoryCache(context);
    }

    public File getCachedOnDisk(Context context, String url, ImageLoader.ImageListener listener){
        if (context == null)
            return null;
        return mStrategy.getCachedOnDisk(context,url,listener);
    }

    public static interface BaseImageListener {


        void onSuccess(Drawable drawable,boolean isGif);

        void onFail();

        void onDownloaded(String filePath,String reNameFilepath);
    }

    public static class ImageListener implements BaseImageListener {
        
        public boolean resourceReady;

        @Override
        public void onSuccess(Drawable drawable, boolean isGif) {
            resourceReady = true;
        }

        @Override
        public void onFail() {
            resourceReady=false;
        }

        @Override
        public void onDownloaded(String filePath, String reNameFilepath) {
            resourceReady = true;
        }

    }



}
