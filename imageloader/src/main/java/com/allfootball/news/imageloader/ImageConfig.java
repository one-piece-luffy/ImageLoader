package com.allfootball.news.imageloader;

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
