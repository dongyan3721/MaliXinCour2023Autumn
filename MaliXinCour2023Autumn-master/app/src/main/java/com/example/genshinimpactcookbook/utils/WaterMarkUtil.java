package com.example.genshinimpactcookbook.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@SuppressLint("SimpleDateFormat")
public class WaterMarkUtil {
    public static Bitmap addTextWatermark(Bitmap src, int textSize, int color, float x, float y, boolean recycle, String location) {
        Objects.requireNonNull(src, "src is null");
        Bitmap ret = src.copy(src.getConfig(), true);
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(src.getWidth() / 20f);
        Rect bounds = new Rect();
        String content = "时    间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n" + "坐    标：" + location;
        /*
        单行文字的实现代码
        paint.getTextBounds(content, 0, content.length(), bounds);
        canvas.drawText(content, x, y, paint);*/

        canvas.translate(x, y);
        StaticLayout myStaticLayout = new StaticLayout(content, paint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        myStaticLayout.draw(canvas);

//        canvas.drawBitmap(bitmap, 0, 0, null);//绘制小图片使用的代码
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    public static Bitmap addPlainWatermark(Bitmap source, String currentLocation) {
        Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(source, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(32);
        // 获取当前系统时间
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 将系统时间和位置信息添加到水印
        float x = 20;
        float y = source.getHeight() - 20;
        canvas.drawText("Time: " + currentTime, x, y, paint);
        y-=20;
        canvas.drawText("Location: "+currentLocation, x, y, paint);
        return result;
    }

}
