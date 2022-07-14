package com.radjago.drivergo.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.ColorInt;

import com.squareup.picasso.Transformation;

public class BackgroundColorTransform implements Transformation {

    @ColorInt
    int mFillColor;

    public BackgroundColorTransform(@ColorInt int color) {
        super();
        mFillColor = color;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        // Create another bitmap that will hold the results of the filter.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap out = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(out);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawColor(mFillColor);
        canvas.drawBitmap(bitmap, 0f, 0f, paint);
        bitmap.recycle();
        return out;
    }

    @Override
    public String key() {
        return "BackgroundColorTransform:" + mFillColor;
    }

}