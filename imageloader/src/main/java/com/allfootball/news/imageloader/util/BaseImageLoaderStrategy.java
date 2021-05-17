
package com.allfootball.news.imageloader.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;


import com.allfootball.news.imageloader.progress.OnProgressListener;

import java.io.File;

/**
 * Created by lihaiyi on 2018/6/14.
 */

public interface BaseImageLoaderStrategy {

    void init();

    void loadImage(Context context, String url, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif);

    void loadImage(Context context, Uri uri, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif);

    void loadImage(Context context, File file, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif);

    void loadImage(Context context, int resourceId, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType, boolean autoPlayGif);

    void loadImage(Context context, String url,ImageLoader.ImageListener listener);

    void loadImage(Context context, String url, ImageView imageView, boolean autoPlayGif,
            ImageLoader.ImageListener listener);

    void loadImage(Context context, String url, ImageView imageView, boolean autoPlayGif,
                   ImageLoader.ImageListener listener, OnProgressListener onProgressListener);

    void loadImage(Context context, String url, int placeholder, int errorResourceId
            , ImageView imageView, boolean roundAsCircle, float roundedCornerRadius
            , int scaleType, int cornerType, boolean autoPlayGif);

    void loadImage(Context context, Uri uri, int placeholder, int errorResourceId
            , ImageView imageView, boolean roundAsCircle, float roundedCornerRadius
            , int scaleType, int cornerType, boolean autoPlayGif);

    void loadImage(Context context, int resourceId, int placeholder, int errorResourceId
            , ImageView imageView, boolean roundAsCircle, float roundedCornerRadius
            , int scaleType, int cornerType, boolean autoPlayGif);

    /**
     * 多图请求，第一张失败就加载第二张
     * @param context
     * @param imageView
     * @param autoPlayGif
     * @param listener
     * @param url 图片数组
     */
    void loadImage(Context context
            , ImageView imageView,  boolean autoPlayGif, final ImageLoader.ImageListener listener,String ... url);

    /**
     *  多图请求，第一张失败就加载第二张
     * @param context
     * @param imageView
     * @param autoPlayGif
     * @param timeout 超时时间 单位毫秒
     * @param url 图片数组
     */
    void loadImage(Context context
            , ImageView imageView,  boolean autoPlayGif, int timeout, String ... url);

    void downloadImage(Context context, String url,ImageLoader.ImageListener listener);

    void clearCache(Context context);

    void clearMemoryCache(Context context);

    File getCachedOnDisk(Context context, String url, ImageLoader.ImageListener listener);

}
