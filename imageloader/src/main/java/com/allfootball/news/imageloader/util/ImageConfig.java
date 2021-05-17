package com.allfootball.news.imageloader.util;

import android.content.Context;
import android.widget.ImageView;

import com.allfootball.news.imageloader.progress.OnProgressListener;

import java.io.File;

public class ImageConfig {
    public int strategyType;


    private ImageConfig(Builder builder) {
        this.strategyType = builder.strategyType;
    }

    public static class Builder {
        int strategyType;

        public Builder strategyType(int strategyType) {
            this.strategyType = strategyType;
            return this;
        }




        public ImageConfig build() {
            return new ImageConfig(this);
        }

    }

}
