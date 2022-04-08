
package com.allfootball.news.imageloader.view;

import com.allfootball.news.imageloader.R;
import com.allfootball.news.imageloader.AspectRatioMeasure;
import com.allfootball.news.imageloader.glide.RoundCornersTransformation.CornerType;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by baofu on 2018/1/10.
 */

public class UnifyImageView extends ImageView {

    private final AspectRatioMeasure.Spec mMeasureSpec = new AspectRatioMeasure.Spec();

    private float mAspectRatio = 0;
    
    int mPlaceHolder, mErrorResourceId;

    boolean mRoundAsCircle;

    float mRoundedCornerRadius;

    int mRoundingBorderWidth;

    int mRoundingBorderColor;

    int mScaleType;
    int mBackgroundImage;

    int mCornerType;


    public UnifyImageView(Context context) {
        super(context);
        init(null);
    }

    public UnifyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UnifyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // todo 处理滑动返回SimpleDraweeView闪烁
        // setLegacyVisibilityHandlingEnabled(true);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.app_image);// TypedArray是一个数组容器

            mPlaceHolder = typedArray.getResourceId(R.styleable.app_image_placeholderImage, 0);// 防止在XML文件里没有定义，就加上了默认值30
            mErrorResourceId = typedArray.getResourceId(R.styleable.app_image_failureImage, 0);// 防止在XML文件里没有定义，就加上了默认值30
            mRoundAsCircle = typedArray.getBoolean(R.styleable.app_image_roundAsCircle, false);// 防止在XML文件里没有定义，就加上了默认值30
            mRoundedCornerRadius = typedArray
                    .getDimension(R.styleable.app_image_roundedCornerRadius, 0);// 防止在XML文件里没有定义，就加上了默认值30
            mRoundingBorderWidth = (int)typedArray
                    .getDimension(R.styleable.app_image_roundingBorderWidth, 0);// 防止在XML文件里没有定义，就加上了默认值30
            mRoundingBorderColor = typedArray.getColor(R.styleable.app_image_roundingBorderColor,
                    0);// 防止在XML文件里没有定义，就加上了默认值30

            mScaleType = typedArray.getInt(R.styleable.app_image_actualImageScaleType, -1);
            mBackgroundImage = typedArray.getInt(R.styleable.app_image_backgroundImage, 0);
            mAspectRatio = typedArray.getFloat(R.styleable.app_image_viewAspectRatio, 0);
            mCornerType = typedArray.getInt(R.styleable.app_image_cornerType,0);
            typedArray.recycle();
            // shape
            if (mRoundingBorderWidth > 0) {
                GradientDrawable drawable = new GradientDrawable();
                if (mRoundedCornerRadius > 0) {
                    //设置不同位置圆角
                    float[] radius;
                    switch (mCornerType) {
                        case CornerType.LEFT_TOP:
                            radius = new float[]{mRoundedCornerRadius, 0, 0, 0};
                            break;
                        case CornerType.LEFT_BOTTOM:
                            radius = new float[]{0, 0, 0, mRoundedCornerRadius};
                            break;
                        case CornerType.RIGHT_TOP:
                            radius = new float[]{0, mRoundedCornerRadius, 0, 0};
                            break;
                        case CornerType.RIGHT_BOTTOM:
                            radius = new float[]{0, 0, mRoundedCornerRadius, 0};
                            break;
                        case CornerType.LEFT:
                            radius = new float[]{mRoundedCornerRadius, 0, 0, mRoundedCornerRadius};
                            break;
                        case CornerType.RIGHT:
                            radius = new float[]{0, mRoundedCornerRadius, mRoundedCornerRadius, 0};
                            break;
                        case CornerType.BOTTOM:
                            radius = new float[]{0, 0, mRoundedCornerRadius, mRoundedCornerRadius};
                            break;
                        case CornerType.TOP:
                            radius = new float[]{mRoundedCornerRadius, mRoundedCornerRadius, 0, 0};
                            break;
                        case CornerType.ALL:
                        default:
                            radius = new float[]{mRoundedCornerRadius, mRoundedCornerRadius, mRoundedCornerRadius, mRoundedCornerRadius};
                    }
                    drawable.setCornerRadii(radius);
                }
                drawable.setStroke(mRoundingBorderWidth, mRoundingBorderColor);
                if (mRoundAsCircle) {
                    drawable.setShape(GradientDrawable.OVAL);
                }
                setBackground(drawable);
                setPadding(mRoundingBorderWidth, mRoundingBorderWidth, mRoundingBorderWidth,
               
                        mRoundingBorderWidth);
            }
            if (mBackgroundImage > 0) {
                setBackgroundResource(mBackgroundImage);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if(mRatio>0){
//
//            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//            int atmost=MeasureSpec.AT_MOST;
//            int EXACTLY=MeasureSpec.EXACTLY;
//            int UNSPECIFIED=MeasureSpec.UNSPECIFIED;
//            if(widthSpecMode==MeasureSpec.UNSPECIFIED&&heightSpecMode== MeasureSpec.AT_MOST){
//                int height = getMeasuredHeight();
//                int width = Math.round(height*mRatio);
//                setMeasuredDimension(width, height);
//            }else if(widthSpecMode==MeasureSpec.UNSPECIFIED&&heightSpecMode== MeasureSpec.UNSPECIFIED){
//                int width = getMeasuredWidth();
//            int height = Math.round(width*mRatio);
//
//
//            setMeasuredDimension(width, height);
//            }
//        }

        mMeasureSpec.width = widthMeasureSpec;
        mMeasureSpec.height = heightMeasureSpec;
        AspectRatioMeasure.updateMeasureSpec(
                mMeasureSpec,
                mAspectRatio,
                getLayoutParams(),
                getPaddingLeft() + getPaddingRight(),
                getPaddingTop() + getPaddingBottom());
        super.onMeasure(mMeasureSpec.width, mMeasureSpec.height);

    }

//    public void setImageURI(int resourceId) {
//        ImageLoader.getInstance().loadImage(getContext(), resourceId, mPlaceHolder, mErrorResourceId,
//                this, mRoundAsCircle, mRoundedCornerRadius, mScaleType, mCornerType, false);
//    }
//
//    public void setImageURI(String url) {
//        ImageLoader.getInstance().loadImage(getContext(), url, mPlaceHolder, mErrorResourceId,
//                this, mRoundAsCircle, mRoundedCornerRadius, mScaleType, mCornerType, false);
//    }
//
//    @Override
//    public void setImageResource(int resId) {
//        setImageURI(resId);
//    }
//
//    @Override
//    public void setImageURI(Uri uri) {
//        // todo
//        ImageLoader.getInstance().loadImage(getContext(), uri, mPlaceHolder, mErrorResourceId, this,
//                mRoundAsCircle, mRoundedCornerRadius, mScaleType, mCornerType, false);
//    }

    public void setAspectRatio(float ratio) {
        // todo
        if (ratio == mAspectRatio) {
            return;
        }
        mAspectRatio = ratio;
        requestLayout();
    }


    public int getPlaceHolder() {
        return mPlaceHolder;
    }

    public int getErrorResourceId() {
        return mErrorResourceId;
    }

    public boolean isRoundAsCircle() {
        return mRoundAsCircle;
    }

    public float getRoundedCornerRadius() {
        return mRoundedCornerRadius;
    }

    public int getRoundingBorderWidth() {
        return mRoundingBorderWidth;
    }

    public int getRoundingBorderColor() {
        return mRoundingBorderColor;
    }

    public int getUnifyScaleType() {
        return mScaleType;
    }

    public void setUnifyImageScaleType(int mScaleType) {
        this.mScaleType = mScaleType;
    }

    public GifDrawable getGifDrawable() {
        Drawable drawable=getDrawable();
        if(drawable instanceof  GifDrawable){
            return (GifDrawable)drawable;
        }
        return null;
    }
}
