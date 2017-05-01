package com.artemkopan.movie.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

public class BlurTransformation implements Transformation<Bitmap> {

    private static int MAX_RADIUS = 25;
    private static int DEFAULT_DOWN_SAMPLING = 1;

    private Context context;
    private BitmapPool bitmapPool;

    private int radius;
    private int sampling;
    private int color;

    public BlurTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool(), MAX_RADIUS, DEFAULT_DOWN_SAMPLING, Color.TRANSPARENT);
    }

    public BlurTransformation(Context context, BitmapPool pool) {
        this(context, pool, MAX_RADIUS, DEFAULT_DOWN_SAMPLING, Color.TRANSPARENT);
    }

    public BlurTransformation(Context context, BitmapPool pool, int radius) {
        this(context, pool, radius, DEFAULT_DOWN_SAMPLING, Color.TRANSPARENT);
    }

    public BlurTransformation(Context context, int radius) {
        this(context, Glide.get(context).getBitmapPool(), radius, DEFAULT_DOWN_SAMPLING, Color.TRANSPARENT);
    }

    public BlurTransformation(Context context, int radius, int sampling) {
        this(context, Glide.get(context).getBitmapPool(), radius, sampling, Color.TRANSPARENT);
    }

    public BlurTransformation(Context context, int radius, int sampling, @ColorInt int color) {
        this(context, Glide.get(context).getBitmapPool(), radius, sampling, color);
    }

    public BlurTransformation(Context context, BitmapPool pool, int radius, int sampling, int color) {
        this.context = context.getApplicationContext();
        bitmapPool = pool;
        this.radius = radius;
        this.sampling = sampling;
        this.color = color;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();
        int scaledWidth = width / sampling;
        int scaledHeight = height / sampling;

        Bitmap bitmap = bitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);

        BlurFactor blurFactor = new BlurFactor();
        blurFactor.width = scaledWidth;
        blurFactor.height = scaledHeight;
        blurFactor.radius = radius;
        blurFactor.color = color;

        Bitmap newBitmap = Blur.of(context, bitmap, blurFactor);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return BitmapResource.obtain(newBitmap, bitmapPool);
    }

    @Override
    public String getId() {
        return "BlurTransformation(radius=" + radius + ", sampling=" + sampling + ")";
    }
}