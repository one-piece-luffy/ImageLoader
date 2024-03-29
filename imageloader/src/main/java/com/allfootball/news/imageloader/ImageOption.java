package com.allfootball.news.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.allfootball.news.imageloader.glide.RoundCornersTransformation;
import com.allfootball.news.imageloader.progress.OnProgressListener;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageOption {
    public Context context;
    public String url;//图片地址
    public int placeholder;//加载过程的图片
    public ImageView imageView;
    public boolean roundAsCircle;//圆形
    public float radiusDp;//圆角，单位dp
    public int scaleType;//缩放类型
    public boolean autoPlayGif;//自动播放gif图片
    public File file;//加载文件图片
    public int errorResourceId;//加载失败的图片
    public ImageLoader.ImageListener listener;//成功失败监听
    public OnProgressListener onProgressListener;//图片加载进度监听
    public String[] urls;//加载多张图片，如果第一张失败则加载第二张图，如果第二张失败则加载弟三张...
    public int timeOutMillisecond;//超时时间
    public int cornerType;//圆角类型：左、上、右、下  ( RoundCornersTransformation.CornerType.LEFT_TOP )
    public int roundingBorderWidth;//圆形图片边框大小，单位dp
    public int roundingBorderColor;//圆形图片边框颜色
    public int resId;//如果没有网络图片则加载的本地图片
    public Transformation<Bitmap> transformation;//自定义tansformation
    public Drawable drawable;
    public Map<String,String> header;
    public boolean asBitmap;


    private ImageOption(Builder builder) {
        this.context = builder.context;
        this.url = builder.url;
        this.placeholder = builder.placeholder;
        this.imageView = builder.imageView;

        this.roundAsCircle = builder.roundAsCircle;
        this.scaleType = builder.scaleType;
        this.autoPlayGif = builder.autoPlayGif;
        this.file = builder.file;
        this.errorResourceId = builder.errorResourceId;
        this.listener = builder.listener;
        this.urls=builder.urls;
        this.onProgressListener=builder.onProgressListener;
        this.cornerType=builder.cornerType;
        this.roundingBorderWidth=builder.roundingBorderWidth;
        this.roundingBorderColor=builder.roundingBorderColor;
        this.resId=builder.resId;
        this.radiusDp=builder.radiusDp;
        this.transformation=builder.transformation;
        this.drawable=builder.drawable;
        this.header=builder.header;
        this.asBitmap=builder.asBitmap;
    }

    public static class Builder {
        private Context context;
        private String url;
        private int placeholder;
        private ImageView imageView;
        private boolean roundAsCircle;
        private int scaleType;
        private boolean autoPlayGif;
        private Drawable drawable;
        File file;
        int errorResourceId;
        private String[] urls;
        ImageLoader.ImageListener listener;
        int timeOutMillisecond;
        OnProgressListener onProgressListener;
        int cornerType;
        int roundingBorderWidth;
        int roundingBorderColor;
        int resId;
        float radiusDp;//圆角，单位px
        Transformation<Bitmap> transformation;
        Map<String,String> header;
        boolean asBitmap;
        public Builder context(Context context) {
            this.context = context;

            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }
        public Builder resId(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }


        public Builder roundAsCircle(boolean roundAsCircle) {
            this.roundAsCircle = roundAsCircle;
            return this;
        }

        public Builder radiusDp(float radiusDp) {
            this.radiusDp = radiusDp;
            return this;
        }

        public Builder scaleType(int scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Builder autoPlayGif(boolean autoPlayGif) {
            this.autoPlayGif = autoPlayGif;
            return this;
        }


        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder errorResourceId(int errorResourceId) {
            this.errorResourceId = errorResourceId;
            return this;
        }

        public Builder listener(ImageLoader.ImageListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder urlArray(String urls[]) {
            this.urls = urls;
            return this;
        }
        public Builder timeOutMillisecond(int timeOutMillisecond) {
            this.timeOutMillisecond = timeOutMillisecond;
            return this;
        }
        public Builder setOnProgressListener(OnProgressListener onProgressListener) {
            this.onProgressListener = onProgressListener;
            return this;
        }
        public Builder cornerType(int  cornerType) {
            this.cornerType = cornerType;
            return this;
        }
        public Builder roundingBorderWidth(int  roundingBorderWidth) {
            this.roundingBorderWidth = roundingBorderWidth;
            return this;
        }
        public Builder roundingBorderColor(int  roundingBorderColor) {
            this.roundingBorderColor = roundingBorderColor;
            return this;
        }
        public Builder transformation( Transformation<Bitmap>  transformation) {
            this.transformation = transformation;
            return this;
        }
        public Builder drawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }
        public Builder header(Map<String,String> header) {
            this.header = header;
            return this;
        }
        public Builder addHeader(String key,String value) {
            if(this.header==null){
                this.header=new HashMap<>();
            }
            this.header.put(key,value);
            return this;
        }

        public Builder asBitmap(boolean asBitmap) {
            this.asBitmap = asBitmap;
            return this;
        }

        public ImageOption build() {
            return new ImageOption(this);
        }
        public void loadImage( Context context) {
            if (context == null){
                return;
            }
            if(context instanceof Activity){
                Activity activity= (Activity) context;
                if(activity.isDestroyed()||activity.isFinishing())
                    return;
            }
            ImageLoader.getInstance().loadImage(context,new ImageOption(this));
        }

    }
}
