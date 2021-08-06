package com.allfootball.news.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.allfootball.news.imageloader.glide.RoundCornersTransformation;
import com.allfootball.news.imageloader.progress.OnProgressListener;

import java.io.File;

public class ImageOption {
    public Context context;
    public String url;//图片地址
    public int placeholder;//加载过程的图片
    public ImageView imageView;
    public boolean roundAsCircle;//圆形
    public float roundedCornerRadius;//圆角，单位px
    public float radiusDp;//圆角，单位px
    public int scaleType;//缩放类型
    public boolean autoPlayGif;//自动播放gif图片
    public File file;//加载文件图片
    public int errorResourceId;//加载失败的图片
    public ImageLoader.ImageListener listener;//成功失败监听
    public OnProgressListener onProgressListener;//图片加载进度监听
    public String[] urls;//加载多张图片，如果第一张失败则加载第二张图，如果第二张失败则加载弟三张...
    public int timeOutMillisecond;//超时时间
    public int cornerType;//圆角类型：左、上、右、下  ( RoundCornersTransformation.CornerType.LEFT_TOP )
    public int roundingBorderWidth;//圆形图片边框大小
    public int roundingBorderColor;//圆形图片边框颜色
    public int resId;//如果没有网络图片则加载的本地图片
    public boolean radiusStrong;//增强版圆角，recyclerview可能会出现圆角不一致的问题，可以设置这个属性


    private ImageOption(Builder builder) {
        this.context = builder.context;
        this.url = builder.url;
        this.placeholder = builder.placeholder;
        this.imageView = builder.imageView;

        this.roundAsCircle = builder.roundAsCircle;
        this.roundedCornerRadius = builder.roundedCornerRadius;
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
        this.radiusStrong=builder.radiusStrong;
    }

    public static class Builder {
        private Context context;
        private String url;
        private int placeholder;
        private ImageView imageView;
        private boolean roundAsCircle;
        private float roundedCornerRadius;
        private int scaleType;
        private boolean autoPlayGif;
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
        boolean radiusStrong;
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

        public Builder radius(float roundedCornerRadius) {
            this.roundedCornerRadius = roundedCornerRadius;
            return this;
        }
        public Builder radiusDp(float radiusDp) {
            this.radiusDp = radiusDp;
            return this;
        }
        public Builder radiusStrong(boolean radiusStrong) {
            this.radiusStrong = radiusStrong;
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


        public ImageOption build() {
            return new ImageOption(this);
        }

    }

}
