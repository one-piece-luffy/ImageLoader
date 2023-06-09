package com.allfootball.news.imageloader.progress;

import android.text.TextUtils;

import com.allfootball.news.imageloader.util.SSLUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by sunfusheng on 2017/6/14.
 */
public class ProgressManager {

    private static final Map<String, OnProgressListener> listenersMap = new HashMap<>();
    private static OkHttpClient okHttpClient;

    private ProgressManager() {
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(15, TimeUnit.SECONDS);//读取超时
            builder.connectTimeout(15, TimeUnit.SECONDS);//连接超时
            builder.writeTimeout(15, TimeUnit.SECONDS);//写入超时
            //支持HTTPS请求，跳过证书验证
            builder.hostnameVerifier(SSLUtil.getInstance().getHostnameVerifier());
            builder.sslSocketFactory(SSLUtil.getInstance().getSSLSocketFactory(), SSLUtil.getInstance().getTrustManager());
            builder .retryOnConnectionFailure(true);
//        builder.proxy(Proxy.NO_PROXY);
            builder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    return response.newBuilder()
                            .body(new ProgressResponseBody(request.url().toString(),
                                    LISTENER, response.body()))
                            .build();
                }
            });

            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public static final ProgressResponseBody.InternalProgressListener LISTENER = new ProgressResponseBody.InternalProgressListener() {

        @Override
        public void onProgress(String url, long bytesRead, long totalBytes) {
            OnProgressListener onProgressListener = getProgressListener(url);
            if (onProgressListener != null) {
                int percentage = (int) ((bytesRead * 1f / totalBytes) * 100f);
                boolean isComplete = percentage >= 100;
                onProgressListener.onProgress(isComplete, percentage, bytesRead, totalBytes);
                if (isComplete) {
                    removeListener(url);
                }
            }
        }
    };

    public static void addListener(String url, OnProgressListener listener) {
        if (!TextUtils.isEmpty(url) && listener != null) {
            listenersMap.put(url, listener);
        }
    }

    public static void removeListener(String url) {
        if (!TextUtils.isEmpty(url)) {
            listenersMap.remove(url);
        }
    }

    public static OnProgressListener getProgressListener(String url) {
        if (TextUtils.isEmpty(url) || listenersMap == null || listenersMap.size() == 0) {
            return null;
        }

        OnProgressListener listenerWeakReference = listenersMap.get(url);
        if (listenerWeakReference != null) {
            return listenerWeakReference;
        }
        return null;
    }
}
