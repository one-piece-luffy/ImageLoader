
package com.allfootball.news.imageloader.fresco;

import android.text.TextUtils;
import android.util.Log;

import com.allfootball.news.imageloader.constant.ImageConstant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by baofu on 2018/3/14.
 */

public class FrescoInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Request request = chain.request();
        String url = request.url().toString();
        Log.i("FrescoInterceptor", "url:" + url);
        if (!TextUtils.isEmpty(url) && url.endsWith(ImageConstant.WEBP_SUFFIX)) {
            url = url.substring(0, url.length() - ImageConstant.WEBP_SUFFIX.length());
            return chain
                    .proceed(request.newBuilder().addHeader("Webp-Support", "0").url(url).build());
        }

        return chain.proceed(request.newBuilder().addHeader("Webp-Support", "1").url(url).build());
    }
}
