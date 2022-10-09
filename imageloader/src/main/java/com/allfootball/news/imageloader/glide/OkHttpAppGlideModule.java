package com.allfootball.news.imageloader.glide;

import static com.allfootball.news.imageloader.progress.ProgressManager.LISTENER;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.allfootball.news.imageloader.fresco.FrescoInterceptor;
import com.allfootball.news.imageloader.progress.ProgressInterceptor;
import com.allfootball.news.imageloader.progress.ProgressManager;
import com.allfootball.news.imageloader.progress.ProgressResponseBody;
import com.allfootball.news.imageloader.util.SSLUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * glide 使用文档
 * https://muyangmin.github.io/glide-docs-cn/doc/configuration.html
 */
@GlideModule
public class OkHttpAppGlideModule extends AppGlideModule {


    @Override
    public boolean isManifestParsingEnabled() {
//        return super.isManifestParsingEnabled();
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setMemoryCache(new LruResourceCache(1024 * 1024 * 20));
        // /data/user/0/com.allfootball.news/cache/image_manager_disk_cache/9ac362afb1982630262bf50025a9246ae3fb77c4426cbd41ed2108cfe8d37562.0
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * 100));
        // 完全自定义路径
        builder.setDiskCache(new
                DiskLruCacheFactory(getDownloadPath(context),
                "glide_cache", 1024 * 1024 * 100));

    }

    public static String getDownloadPath(Context context) {

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return context.getFilesDir().getAbsolutePath();
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (file != null)
            return file.getAbsolutePath();
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (file != null)
            return file.getAbsolutePath();
        return context.getFilesDir().getAbsolutePath();
    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory((Call.Factory) ProgressManager.getOkHttpClient());
        registry.replace(GlideUrl.class, InputStream.class, factory);
    }


}

