
package com.allfootball.news.imageloader.glide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.allfootball.news.imageloader.R;
import com.allfootball.news.imageloader.constant.ImageConstant;
import com.allfootball.news.imageloader.progress.ProgressManager;
import com.allfootball.news.imageloader.BaseImageStrategy;
import com.allfootball.news.imageloader.ImageConfig;
import com.allfootball.news.imageloader.ImageLoader;
import com.allfootball.news.imageloader.ImageOption;
import com.allfootball.news.imageloader.util.ImageDownloadExecutor;
import com.allfootball.news.imageloader.util.ImageLoaderUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by baofu on 2018/6/14.
 */

public class GlideImageLoaderStrategy implements BaseImageStrategy {

    public static final String TAG = "GlideStrategy";

    @Override
    public void init(ImageConfig config) {
//        ViewTarget.setTagId(R.id.glide_tag);
    }

    @Override
    public void loadImage(Context context, ImageOption option) {
        if (context == null)
            return;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing())
                return;
        }
        if (option.urls != null) {
            loadImage(context, option.imageView, option.timeOutMillisecond, option.urls);
        } else {
            startLoad(context, option);
        }

    }


    /**
     * 多图请求，第一张失败或者超过预定的阀值还没加载出来就加载第下一张，以此类推，最后那个url的图片先加载出来就显示哪个
     * eg:第一个url0.5秒没显示出来就去加载第二个url，第二个url0.5秒没显示出来就去加载第三个，以此类推，最后那个图片先下载下来就显示哪个
     *
     * @param context
     * @param imageView
     * @param timeout   超时时间 单位毫秒,默认10秒
     * @param url       图片数组
     */
    public void loadImage(final Context context, final ImageView imageView, final int timeout, final String... url) {

        imageView.setTag(imageView.getId(), null);
        final String temp = url[0];
        loadImage(context, url[0], new ImageLoader.ImageListener() {
            @Override
            public void onSuccess(Drawable drawable, boolean isGif) {
                super.onSuccess(drawable, isGif);
                if (imageView.getTag(imageView.getId()) == null) {
                    imageView.setTag(imageView.getId(), isGif);
                    imageView.setImageDrawable(drawable);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                Log.e(TAG, "load url:" + temp);
            }
        });

        final Handler checkTimeOut = new Handler();
        Runnable runnable = new Runnable() {
            int index = 1;

            @Override
            public void run() {
                checkTimeOut.removeCallbacks(this);
                if (imageView.getTag(imageView.getId()) == null) {
                    if (index < url.length - 1) {
                        final String temp = url[index];
                        loadImage(context, url[index++], new ImageLoader.ImageListener() {
                            @Override
                            public void onSuccess(Drawable drawable, boolean isGif) {
                                super.onSuccess(drawable, isGif);
                                if (imageView.getTag(imageView.getId()) == null) {
                                    imageView.setTag(imageView.getId(), isGif);
                                    imageView.setImageDrawable(drawable);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                }
                                Log.e(TAG, "load url:" + temp);
                            }
                        });
                        int timeOutMillisecond = timeout;
                        if (timeOutMillisecond <= 0) {
                            timeOutMillisecond = 10 * 1000;
                        }
                        checkTimeOut.postDelayed(this, timeOutMillisecond);
                    } else if (index == url.length - 1) {
                        final String temp = url[index];
                        loadImage(context, url[index], new ImageLoader.ImageListener() {
                            @Override
                            public void onSuccess(Drawable drawable, boolean isGif) {
                                super.onSuccess(drawable, isGif);
                                if (imageView.getTag(imageView.getId()) == null) {
                                    imageView.setTag(imageView.getId(), isGif);
                                    imageView.setImageDrawable(drawable);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                }
                                Log.e(TAG, "load url:" + temp);
                            }
                        });
                    }

                }
            }
        };
        checkTimeOut.postDelayed(runnable, timeout);
    }

    /**
     * 没有设置imageview的图像
     *
     * @param context
     * @param url
     * @param listener
     */
    public void loadImage(final Context context, String url,
                          final ImageLoader.ImageListener listener) {

        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource,
                                        @Nullable Transition<? super Drawable> transition) {

                if (resource instanceof GifDrawable) {
                    if (listener != null) {
                        listener.onSuccess(resource, true);
                    }
                    // Do things with GIF here.
                } else {
                    if (listener != null) {
                        listener.onSuccess(resource, false);
                    }
                }

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if (listener != null) {
                    listener.onFail();
                }
            }
        });
    }


    private void setBorder(ImageOption option) {
        if (option.roundingBorderWidth > 0) {
            int boarder = ImageLoaderUtil.dip2px(option.context, option.roundingBorderWidth);
            GradientDrawable drawable = new GradientDrawable();

            drawable.setStroke(boarder, option.roundingBorderColor);
            if (option.roundAsCircle) {
                drawable.setShape(GradientDrawable.OVAL);
            }
            option.imageView.setBackground(drawable);
            option.imageView.setPadding(boarder, boarder, boarder, boarder);
        }
    }

    private void startLoad(final Context context, final ImageOption option) {
        if (context == null)
            return;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing())
                return;
        }
        String tempUrl = option.url;
        if (!TextUtils.isEmpty(tempUrl) && tempUrl.endsWith(ImageConstant.WEBP_SUFFIX)) {

            tempUrl = tempUrl.substring(0, tempUrl.length() - ImageConstant.WEBP_SUFFIX.length());

        }
        ProgressManager.addListener(tempUrl, option.onProgressListener);


        float radius = 0;
        if (option.radiusDp > 0) {
            radius = ImageLoaderUtil.dip2px(context, option.radiusDp);
        }
        RequestOptions requestOptions = getOptions(context, radius, option);

        if (TextUtils.isEmpty(option.url)) {
            if (option.drawable != null) {
                Glide.with(context).load(option.drawable).apply(requestOptions)
                        .into(option.imageView);
            } else if(option.file!=null){
                Glide.with(context).asBitmap().load(option.file).apply(requestOptions)
                        .into(option.imageView);
            }else {
                if (option.imageView == null) {
                    Glide.with(context).asBitmap().load(option.resId).apply(requestOptions)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    if (context == null)
                                        return;
                                    if (context instanceof Activity) {
                                        Activity activity = (Activity) context;
                                        if (activity.isDestroyed() || activity.isFinishing())
                                            return;
                                    }

                                    if (option.listener != null) {
                                        option.listener.onBitMapSuccess(resource, false);
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    if (context == null)
                                        return;
                                    if (context instanceof Activity) {
                                        Activity activity = (Activity) context;
                                        if (activity.isDestroyed() || activity.isFinishing())
                                            return;
                                    }
                                    if (option.imageView != null) {
                                        option.imageView.setImageDrawable(errorDrawable);
                                    }
                                    if (option.listener != null) {
                                        option.listener.onFail();
                                    }
                                }
                            });

                } else {
                    Glide.with(context).asBitmap().load(option.resId).apply(requestOptions)
                            .into(option.imageView);
                }

            }

            return;
        }
        GlideUrl glideUrl=null;
        if (option.header != null) {
            Iterator<Map.Entry<String, String>> it = option.header.entrySet().iterator();
            LazyHeaders.Builder builder = new LazyHeaders.Builder();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                if (entry == null)
                    continue;
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                builder.addHeader(key, value);
            }
            glideUrl = new GlideUrl(option.url, builder.build());
        }
        if (option.asBitmap) {
            Object obj=glideUrl==null?option.url:glideUrl;
            Glide.with(context).asBitmap().load(obj).apply(requestOptions)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (context == null)
                                return;
                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;
                                if (activity.isDestroyed() || activity.isFinishing())
                                    return;
                            }

                            if (option.listener != null) {
                                option.listener.onBitMapSuccess(resource, false);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            if (context == null)
                                return;
                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;
                                if (activity.isDestroyed() || activity.isFinishing())
                                    return;
                            }
                            if (option.imageView != null) {
                                option.imageView.setImageDrawable(errorDrawable);
                            }
                            if (option.listener != null) {
                                option.listener.onFail();
                            }
                        }
                    });
        } else {
            Object obj=glideUrl==null?option.url:glideUrl;
            Glide.with(context).load(obj).apply(requestOptions)
                    .into(new ImageViewTarget<Drawable>(option.imageView) {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                                                    @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                            if (context == null)
                                return;
                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;
                                if (activity.isDestroyed() || activity.isFinishing())
                                    return;
                            }
                            if (resource instanceof GifDrawable) {

                                GifDrawable gifDrawable = (GifDrawable) resource;

                                if (option.imageView != null) {
                                    if (option.autoPlayGif) {
                                        gifDrawable.start();
                                        Log.i(TAG, "gif frame index:" + gifDrawable.getFrameIndex());
                                        Log.i(TAG, "gif frame is running:" + gifDrawable.isRunning());
                                        if (!gifDrawable.isRunning() && gifDrawable.getFrameIndex() > -1) {
                                            gifDrawable.startFromFirstFrame();
                                        }

                                    }
                                    option.imageView.setImageDrawable(gifDrawable);
                                    setBorder(option);
                                }

                                if (option.listener != null) {
                                    option.listener.onSuccess(resource, true);
                                }
                                // Do things with GIF here.
                            } else {
                                if (option.imageView != null) {
                                    option.imageView.setImageDrawable(resource);

                                    setBorder(option);
                                }
                                if (option.listener != null) {
                                    option.listener.onSuccess(resource, false);
                                }
                            }
                        }

                        @Override
                        protected void setResource(@Nullable Drawable resource) {
                            if (context == null)
                                return;
                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;
                                if (activity.isDestroyed() || activity.isFinishing())
                                    return;
                            }
                            if (option.imageView != null) {
                                option.imageView.setImageDrawable(resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            if (context == null)
                                return;
                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;
                                if (activity.isDestroyed() || activity.isFinishing())
                                    return;
                            }
                            if (option.imageView != null) {
                                option.imageView.setImageDrawable(errorDrawable);
                            }
                            if (option.listener != null) {
                                option.listener.onFail();
                            }
                        }
                    });


        }


    }


    public void downloadImage2(final Context context, final String url,
                              final ImageLoader.ImageListener listener) {
        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource,
                                        @Nullable Transition<? super Drawable> transition) {
                getCachedOnDisk(context, url, listener);

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if (listener != null) {
                    listener.onFail();
                }
            }
        });
    }

    @Override
    public void downloadImage(final Context context, final String url,String fileName,
                              final ImageLoader.ImageListener listener) {
        final Handler handler = new Handler();
        ImageDownloadExecutor.execute(() -> {
            final File f;
            try {
                f = Glide.with(context).downloadOnly().load(url)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                String path = ImageLoader.getInstance().getConfig().downloadPath;

                File dirFile = new File(path);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                String newPath=path + "/";
                if(TextUtils.isEmpty(fileName)){
                    newPath+=System.currentTimeMillis()
                            + ".jpg";
                }else {
                    newPath+=fileName;
                }

                final String tempNewPath = newPath;
                copyFile(f.getAbsolutePath(), newPath);
                if (listener != null) {
                    listener.onDownloaded(f.getAbsolutePath(), tempNewPath);
                    Log.e(TAG, "download file path:" + f.getAbsolutePath());

                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFail();
            }
        });

    }

    @Override
    public void clearCache(final Context context) {

        ImageDownloadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();

                File file=new File(ImageLoader.getInstance().getConfig().downloadPath);
                File[] list=file.listFiles();
                if(list!=null&&list.length>0){
                    for (File f:list){
                        f.delete();
                    }
                }

            }
        });

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // 已在主线程中，可以更新UI
                Glide.get(context).clearMemory();
            }
        });

    }

    @Override
    public void clearMemoryCache(final Context context) {
        Glide.get(context).clearMemory();
    }

    public File getCachedOnDisk(final Context context, final String url) {


        new Thread() {
            @Override
            public void run() {
                super.run();
                // FutureTarget<File> futureTarget =
                // Glide.with(BaseApplication.getInstance()).load("https://img1.dongqiudi.com/fastdfs3/M00/13/3E/ChOxM1sm76iASLuEAAJEaYEwcJI327.jpg").downloadOnly(100,
                // 100);
                // try {
                // File file = futureTarget.get();
                // String path = file.getAbsolutePath();
                // return ;
                // } catch (InterruptedException e) {
                // e.printStackTrace();
                // } catch (ExecutionException e) {
                // e.printStackTrace();
                // }

                try {
                    File f = Glide.with(context).downloadOnly().load(
                            "https://img1.dongqiudi.com/fastdfs3/M00/13/2C/ChOxM1smvWaAfTQ8AACLY0UfZi4285.jpg")
                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()// needs to be
                            // called

                            // on background thread
                            ;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
        return null;

    }

    @Override
    public File getCachedOnDisk(final Context context, final String url,
                                final ImageLoader.ImageListener imageListener) {

        final Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    final File f = Glide.with(context).downloadOnly().load(url)
                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    String path = f.getParentFile().getAbsolutePath() + "/download";

                    File dirFile = new File(path);
                    if (!dirFile.exists()) {
                        dirFile.mkdir();
                    }
                    final String newPath = dirFile.getAbsolutePath() + "/" + System.currentTimeMillis()
                            + ".jpg";
                    copyFile(f.getAbsolutePath(), newPath);
                    if (f != null && imageListener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageListener.onDownloaded(f.getAbsolutePath(), newPath);
                            }
                        });
                        Log.e(TAG, "download file path:" + f.getAbsolutePath());

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
        return null;

    }

    @SuppressLint("CheckResult")
    private RequestOptions getOptions(Context context, float roundedCornerRadius, ImageOption imageOption) {
        RequestOptions options = new RequestOptions();
        if (imageOption.transformation != null) {
            options.transform(imageOption.transformation);
        } else if (((int) roundedCornerRadius) > 0) {
            RoundCornersTransformation transform = new RoundCornersTransformation(context, roundedCornerRadius, imageOption.cornerType);
            options.transform(new CenterCrop(), transform);
        } else {

            switch (imageOption.scaleType) {
                case -1:
                    break;
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    options.transform(new FitCenter());
                    break;
                case 3:
                    break;
                case 4:

                    break;
                case 5:
                    options.transform(new CenterInside());
                    break;
                case 6:
                    options.transform(new CenterCrop());
                    break;
                case 7:
                    break;
                case 8:
                    break;
            }
        }

        options.placeholder(imageOption.placeholder).error(imageOption.errorResourceId);
        if (!imageOption.autoPlayGif) {
            options.dontAnimate();
        }
        if (imageOption.roundAsCircle) {
            options.circleCrop();
        }

        //跳过缓存
//        options.diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true);

        //仅用缓存
//          options.onlyRetrieveFromCache(true);
        return options;
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：data/video/xxx.jpg
     * @param newPath String 复制后路径 如：data/oss/xxxx.jpg
     * @return boolean
     */
    private void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldPath); // 读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class CheckTimeOut extends Handler {
        public ImageLoader.ImageListener listener;
        public boolean resourceReady = false;

        public CheckTimeOut(ImageLoader.ImageListener listener) {
            this.listener = listener;
        }
    }

}
