
package com.allfootball.news.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.allfootball.news.imageloader.R;
import com.allfootball.news.imageloader.constant.ImageConstant;
import com.allfootball.news.imageloader.progress.OnProgressListener;
import com.allfootball.news.imageloader.progress.ProgressManager;
import com.allfootball.news.imageloader.util.BaseImageLoaderStrategy;
import com.allfootball.news.imageloader.util.ImageLoader;
import com.allfootball.news.view.UnifyImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by lihaiyi on 2018/6/14.
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {

    public static final String TAG = "GlideStrategy";

    @Override
    public void init() {
        ViewTarget.setTagId(R.id.glide_tag);
    }

    @Override
    public void loadImage(Context context, String url, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType,
            boolean autoPlayGif) {
        if (context == null)
            return;
        RequestOptions options = getOptions(placeholder, errorResourceId, roundAsCircle,
                roundedCornerRadius, scaleType, autoPlayGif);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    @Override
    public void loadImage(Context context, Uri uri, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType,
            boolean autoPlayGif) {
        if (context == null)
            return;
        RequestOptions options = getOptions(placeholder, errorResourceId, roundAsCircle,
                roundedCornerRadius, scaleType, autoPlayGif);
        // GenericTransitionOptions.with(R.anim.fade_in)
        // Glide.with(context).load(uri).transition(DrawableTransitionOptions.withCrossFade()).apply(options).into(imageView);
        Glide.with(context).load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, File file, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType,
            boolean autoPlayGif) {
        if (context == null)
            return;
        RequestOptions options = getOptions(placeholder, errorResourceId, roundAsCircle,
                roundedCornerRadius, scaleType, autoPlayGif);
        Glide.with(context).load(file).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resourceId, int placeholder, int errorResourceId,
            ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType,
            boolean autoPlayGif) {
        if (context == null)
            return;
        RequestOptions options = getOptions(placeholder, errorResourceId, roundAsCircle,
                roundedCornerRadius, scaleType, autoPlayGif);
        Glide.with(context).load(resourceId).apply(options).into(imageView);

    }

    @Override
    public void loadImage(Context context, String url, int placeholder, int errorResourceId,
                          ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType,
                          int cornerType, boolean autoPlayGif) {
        if (context == null)
            return;
        RequestOptions options = getOptions(context, placeholder, errorResourceId, roundAsCircle,
                roundedCornerRadius, scaleType, cornerType, autoPlayGif);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    @Override
    public void loadImage(Context context, Uri uri, int placeholder, int errorResourceId,
                          ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType,
                          int cornerType, boolean autoPlayGif) {
        if (context == null)
            return;
        RequestOptions options = getOptions(context, placeholder, errorResourceId, roundAsCircle,
                roundedCornerRadius, scaleType, cornerType, autoPlayGif);
        // GenericTransitionOptions.with(R.anim.fade_in)
        // Glide.with(context).load(uri).transition(DrawableTransitionOptions.withCrossFade()).apply(options).into(imageView);

        Glide.with(context).load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resourceId, int placeholder, int errorResourceId,
                          ImageView imageView, boolean roundAsCircle, float roundedCornerRadius, int scaleType,
                          int cornerType, boolean autoPlayGif) {
        if (context == null)
            return;
        RequestOptions options = getOptions(context, placeholder, errorResourceId, roundAsCircle,
                roundedCornerRadius, scaleType, cornerType, autoPlayGif);
        Glide.with(context).load(resourceId).apply(options).into(imageView);
    }

    private RequestBuilder getRetryRequest(Context context, RequestOptions options, int index,
            String... url) {
        if (context == null)
            return null;
        if (index >= url.length) {
            return null;
        } else if (index == url.length - 1) {
            return Glide.with(context).load(url[index]).apply(options);
        } else {
            return Glide.with(context).load(url[index])
                    .error(getRetryRequest(context, options, index + 1, url)).apply(options);
        }
    }

    /**
     * 多图请求，第一张失败就加载第二张
     * @param context
     * @param imageView
     * @param autoPlayGif
     * @param listener
     * @param url 图片数组
     */
    @Override
    public void loadImage(Context context, ImageView imageView, boolean autoPlayGif,
                          final ImageLoader.ImageListener listener, String... url) {
        RequestOptions options = new RequestOptions();
        if (imageView instanceof UnifyImageView) {
            UnifyImageView unifyImageView = (UnifyImageView)imageView;
            options = getOptions(unifyImageView.getPlaceHolder(),
                    unifyImageView.getErrorResourceId(), unifyImageView.isRoundAsCircle(),
                    unifyImageView.getRoundedCornerRadius(), unifyImageView.getUnifyScaleType(),
                    autoPlayGif);
        }
        Glide.with(context).load(url[0])
                .error(getRetryRequest(context,options,1,url))
                .into(new ImageViewTarget<Drawable>(imageView) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                            @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        if (imageView == null) {
                            return;
                        }
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (resource instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable)resource;
                            if (autoPlayGif) {
                                gifDrawable.start();
                                if (!gifDrawable.isRunning() && gifDrawable.getFrameIndex() > -1) {
                                    gifDrawable.startFromFirstFrame();
                                }
                            }
                            imageView.setImageDrawable(gifDrawable);
                            if (listener != null) {
                                listener.onSuccess(resource, true);
                            }
                            // Do things with GIF here.
                        } else {
                            imageView.setImageDrawable(resource);
                            if (listener != null) {
                                listener.onSuccess(resource, false);
                            }
                        }

                    }

                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (listener != null) {
                            listener.onFail();
                        }
                    }
                });
//                Glide.with(context).clear(imageView);

    }


    /**
     * 多图请求，第一张失败或者超过预定的阀值还没加载出来就加载第下一张，以此类推，最后那个url的图片先加载出来就显示哪个
     * eg:第一个url0.5秒没显示出来就去加载第二个url，第二个url0.5秒没显示出来就去加载第三个，以此类推，最后那个图片先下载下来就显示哪个
     * 
     * @param context
     * @param imageView
     * @param autoPlayGif
     * @param timeout 超时时间 单位毫秒
     * @param url 图片数组
     */
    @Override
    public void loadImage(Context context, ImageView imageView, boolean autoPlayGif, int timeout, String... url) {
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

        Handler checkTimeOut = new Handler();
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
                        checkTimeOut.postDelayed(this, timeout);
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
     * @param context
     * @param url
     * @param listener
     */
    @Override
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

    @Override
    public void loadImage(Context context, String url, final ImageView imageView,
            final boolean autoPlayGif, final ImageLoader.ImageListener listener) {
        RequestOptions options = new RequestOptions();
        if (imageView instanceof UnifyImageView) {
            UnifyImageView unifyImageView = (UnifyImageView)imageView;
            options = getOptions(unifyImageView.getPlaceHolder(),
                    unifyImageView.getErrorResourceId(), unifyImageView.isRoundAsCircle(),
                    unifyImageView.getRoundedCornerRadius(), unifyImageView.getUnifyScaleType(),
                    autoPlayGif);

        }
        Glide.with(context).load(url).apply(options)
                .into(new ImageViewTarget<Drawable>(imageView) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                            @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        if (imageView == null) {
                            return;
                        }
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (resource instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable)resource;
                            if (autoPlayGif) {
                                gifDrawable.start();
                                if (!gifDrawable.isRunning() && gifDrawable.getFrameIndex() > -1) {
                                    gifDrawable.startFromFirstFrame();
                                }
                            }
                            imageView.setImageDrawable(gifDrawable);
                            if (listener != null) {
                                listener.onSuccess(resource, true);
                            }
                            // Do things with GIF here.
                        } else {
                            imageView.setImageDrawable(resource);
                            if (listener != null) {
                                listener.onSuccess(resource, false);
                            }
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Drawable resource) {
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
    public void loadImage(Context context, String url, final ImageView imageView,
            final boolean autoPlayGif, final ImageLoader.ImageListener listener,
            OnProgressListener onProgressListener) {
        String tempUrl = url;
        if (!TextUtils.isEmpty(url) && url.endsWith(ImageConstant.WEBP_SUFFIX)) {

            tempUrl = url.substring(0, url.length() - ImageConstant.WEBP_SUFFIX.length());

        }
        ProgressManager.addListener(tempUrl, onProgressListener);

        RequestOptions options = new RequestOptions();
        if (imageView instanceof UnifyImageView) {
            UnifyImageView unifyImageView = (UnifyImageView)imageView;
            options = getOptions(unifyImageView.getPlaceHolder(),
                    unifyImageView.getErrorResourceId(), unifyImageView.isRoundAsCircle(),
                    unifyImageView.getRoundedCornerRadius(), unifyImageView.getUnifyScaleType(),
                    autoPlayGif);

        }
        Glide.with(context).load(url).apply(options)
                .into(new ImageViewTarget<Drawable>(imageView) {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                            @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        if (imageView == null) {
                            return;
                        }
                        if (resource instanceof GifDrawable) {

                            GifDrawable gifDrawable = (GifDrawable)resource;
                            if (autoPlayGif) {
                                gifDrawable.start();
                                Log.e(TAG, "gif frame index:" + gifDrawable.getFrameIndex());
                                Log.e(TAG, "gif frame is running:" + gifDrawable.isRunning());
                                if (!gifDrawable.isRunning() && gifDrawable.getFrameIndex() > -1) {
                                    gifDrawable.startFromFirstFrame();
                                }
                                // if(!gifDrawable.isRunning()){
                                // gifDrawable.startFromFirstFrame();
                                // }

                            }
                            imageView.setImageDrawable(gifDrawable);
                            if (listener != null) {
                                listener.onSuccess(resource, true);
                            }
                            // Do things with GIF here.
                        } else {
                            imageView.setImageDrawable(resource);
                            if (listener != null) {
                                listener.onSuccess(resource, false);
                            }
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Drawable resource) {

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
    public void downloadImage(final Context context, final String url,
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
    public void clearCache(final Context context) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Glide.get(context).clearDiskCache();

            }
        }.start();
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
                    String newPath = dirFile.getAbsolutePath() + "/" + System.currentTimeMillis()
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

    private RequestOptions getOptions(int placeholder, int errorResourceId, boolean roundAsCircle,
            float roundedCornerRadius, int scleType, boolean autoPlayGif) {
        RequestOptions options = new RequestOptions();
        if (((int)roundedCornerRadius) > 0) {
            options.transforms(new CenterCrop(), new RoundedCorners((int)roundedCornerRadius));

        } else {

            switch (scleType) {
                case -1:
                    break;
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    options.transforms(new FitCenter());
                    break;
                case 3:
                    break;
                case 4:

                    break;
                case 5:
                    options.transforms(new CenterInside());
                    break;
                case 6:
                    options.transforms(new CenterCrop());
                    break;
                case 7:
                    break;
                case 8:
                    break;
            }
        }

        options.placeholder(placeholder).error(errorResourceId);
        if (!autoPlayGif) {
            options.dontAnimate();
        }
        if (roundAsCircle) {
            options.circleCrop();
        }

        //跳过缓存
//        options.diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true);

        //仅用缓存
//          options.onlyRetrieveFromCache(true);
        return options;
    }

    private RequestOptions getOptions(Context context, int placeholder, int errorResourceId, boolean roundAsCircle,
                                      float roundedCornerRadius, int scleType, int cornerType, boolean autoPlayGif) {
        RequestOptions options = new RequestOptions();
        if (((int)roundedCornerRadius) > 0) {
            RoundCornersTransformation transformation = new RoundCornersTransformation(context, roundedCornerRadius, cornerType);
            options.transforms(new CenterCrop(), transformation);

        } else {

            switch (scleType) {
                case -1:
                    break;
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    options.transforms(new FitCenter());
                    break;
                case 3:
                    break;
                case 4:

                    break;
                case 5:
                    options.transforms(new CenterInside());
                    break;
                case 6:
                    options.transforms(new CenterCrop());
                    break;
                case 7:
                    break;
                case 8:
                    break;
            }
        }

        options.placeholder(placeholder).error(errorResourceId);
        if (!autoPlayGif) {
            options.dontAnimate();
        }
        if (roundAsCircle) {
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
        public boolean resourceReady=false;

        public CheckTimeOut(ImageLoader.ImageListener listener) {
            this.listener = listener;
        }
    }

}
