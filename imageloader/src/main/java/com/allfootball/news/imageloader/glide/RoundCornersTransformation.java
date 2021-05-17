package com.allfootball.news.imageloader.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * create by zhangxia 2018-08-10
 * 设置图片不同类型圆角
 */
public class RoundCornersTransformation extends BitmapTransformation {

    private static final String ID = "com.allfootball.news.imageloader.glide.RoundCornersTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private float mRadius;
    private float mDiameter;
    private int mCornerType = CornerType.ALL;

    /**
     * 圆角类型
     */
    @IntDef({
            CornerType.ALL,
            CornerType.LEFT_TOP,
            CornerType.LEFT_BOTTOM,
            CornerType.RIGHT_TOP,
            CornerType.RIGHT_BOTTOM,
            CornerType.LEFT,
            CornerType.RIGHT,
            CornerType.BOTTOM,
            CornerType.TOP,
    })

    public @interface CornerType {

        //所有角
        int ALL = 0;
        //左上
        int LEFT_TOP = 1;
        //左下
        int LEFT_BOTTOM = 2;
        //右上
        int RIGHT_TOP = 3;
        //右下
        int RIGHT_BOTTOM = 4;
        //左侧
        int LEFT = 5;
        //右侧
        int RIGHT = 6;
        //下侧
        int BOTTOM = 7;
        //上侧
        int TOP = 8;
    }

    public RoundCornersTransformation(Context context, float radius, int type) {
        mCornerType = type;
        mRadius = radius;
        mDiameter = mRadius * 2;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int outWidth, int outHeight) {

        Bitmap.Config safeConfig = getAlphaSafeConfig(inBitmap);
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);
        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), safeConfig);

        result.setHasAlpha(true);

        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawRoundRect(canvas, paint, result.getWidth(), result.getHeight());
        clear(canvas);

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    @NonNull
    private static Bitmap.Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16.equals(inBitmap.getConfig())) {
                return Bitmap.Config.RGBA_F16;
            }
        }

        return Bitmap.Config.ARGB_8888;
    }

    private static Bitmap getAlphaSafeBitmap(@NonNull BitmapPool pool, @NonNull Bitmap maybeAlphaSafe) {
        Bitmap.Config safeConfig = getAlphaSafeConfig(maybeAlphaSafe);
        if (safeConfig.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap = pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), safeConfig);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*paint*/);

        return argbBitmap;
    }

    private static void clear(Canvas canvas) {
        canvas.setBitmap(null);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        switch (mCornerType) {
            case CornerType.LEFT_TOP:
                drawLeftTopCorner(canvas, paint, width, height);
                break;
            case CornerType.LEFT_BOTTOM:
                drawLeftBottomCorner(canvas, paint, width, height);
                break;
            case CornerType.RIGHT_TOP:
                drawRightTopCorner(canvas, paint, width, height);
                break;
            case CornerType.RIGHT_BOTTOM:
                drawRightBottomCorner(canvas, paint, width, height);
                break;
            case CornerType.LEFT:
                drawLeftCorner(canvas, paint, width, height);
                break;
            case CornerType.RIGHT:
                drawRightCorner(canvas, paint, width, height);
                break;
            case CornerType.BOTTOM:
                drawBottomCorner(canvas, paint, width, height);
                break;
            case CornerType.TOP:
                drawTopCorner(canvas, paint, width, height);
                break;
            case CornerType.ALL:
            default:
                canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius, paint);
                break;
        }
    }

    /**
     * 画左上角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawLeftTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
    }

    /**
     * 画左下角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawLeftBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右上角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawRightTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画右下角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawRightBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(0, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画左角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawLeftCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height - mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawRightCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height - mRadius), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画上角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, mRadius, width, height), paint);
        canvas.drawRect(new RectF(mRadius, 0, width - mRadius, mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画下角
     * @param canvas
     * @param paint
     * @param width
     * @param height
     */
    private void drawBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RoundCornersTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

}

