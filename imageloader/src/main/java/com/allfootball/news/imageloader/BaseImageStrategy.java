
package com.allfootball.news.imageloader;

import android.content.Context;


import java.io.File;

/**
 * Created by baofu on 2018/6/14.
 */

public interface BaseImageStrategy {

    void init(ImageConfig config);

    void loadImage(Context context, ImageOption option);

    void downloadImage(Context context, String url,String fileName,ImageLoader.ImageListener listener);

    void clearCache(Context context);

    void clearMemoryCache(Context context);

    File getCachedOnDisk(Context context, String url, ImageLoader.ImageListener listener);

}
