package com.allfootball.news.imageloader.progress;


import android.os.Trace;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lihaiyi on 2018/6/22.
 */

public class ProgressInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();
        Log.i("ProgressInterceptor", "url:" + url);
//        if (!TextUtils.isEmpty(url) && url.endsWith(AppConstant.WEBP_SUFFIX)) {
//            url = url.substring(0, url.length() - AppConstant.WEBP_SUFFIX.length());
//
//        }
        return response.newBuilder()
                .body(new ProgressResponseBody(url, ProgressManager.LISTENER, response.body()))
                .build();
    }
}
