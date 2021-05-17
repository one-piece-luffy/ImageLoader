
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

    void init(ImageConfig config);

    void loadImage(Context context,ImageOption option);

    void downloadImage(Context context, String url,ImageLoader.ImageListener listener);

    void clearCache(Context context);

    void clearMemoryCache(Context context);

    File getCachedOnDisk(Context context, String url, ImageLoader.ImageListener listener);

}
