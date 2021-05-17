package com.allfootball.news.imageloader.util;

import android.content.Context;
import android.widget.ImageView;

import com.allfootball.news.imageloader.progress.OnProgressListener;

import java.io.File;

public class ImageOption {
    public Context context;
    public String url;
    public int placeholder;
    public int failRes;
    public ImageView imageView;
    public boolean roundAsCircle;
    public float roundedCornerRadius;
    public int scaleType;
    public boolean autoPlayGif;
    public File file;
    public int errorResourceId;
    public ImageLoader.ImageListener listener;
    public OnProgressListener onProgressListener;
    public String[] urls;
    public int timeOutMillisecond;
    public int cornerType;
    public int roundingBorderWidth;

    public int roundingBorderColor;


    private ImageOption(Builder builder) {
        this.context = builder.context;
        this.url = builder.url;
        this.placeholder = builder.placeholder;
        this.failRes = builder.failRes;
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
    }

    public static class Builder {
        private Context context;
        private String url;
        private int placeholder;
        private int failRes;
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

        public Builder failRes(int failRes) {
            this.failRes = failRes;
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
