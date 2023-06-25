package com.allfootball.news.imageloader;

public class ImageConfig {
    public int strategyType;

    public String downloadPath;

    private ImageConfig(Builder builder) {
        this.strategyType = builder.strategyType;
        this.downloadPath=builder.downloadPath;
    }

    public static class Builder {
        int strategyType;
        String downloadPath;

        public Builder strategyType(int strategyType) {
            this.strategyType = strategyType;
            return this;
        }
        public Builder downloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
            return this;
        }




        public ImageConfig build() {
            return new ImageConfig(this);
        }

    }

}
