package com.allfootball.news.imageloader.constant;

public enum ScaleType {

    None(-1),
    FitCenter(2),
    CenterInside(5),
    CenterCrop(6);
    public final int value;

    //3.提供枚举形参构造器
    ScaleType(int value) {
        this.value = value;
    }
}
