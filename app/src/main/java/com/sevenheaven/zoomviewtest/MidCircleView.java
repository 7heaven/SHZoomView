package com.sevenheaven.zoomviewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by caifangmao on 15/4/7.
 */
public class MidCircleView extends View {

    private Bitmap cache;
    private Canvas cacheCanvas;
    private ArrayList<Point> cachePoint;

    private int r = 35;

    private int downX;
    private int downY;

    private int drawX;
    private int drawY;

    private int scale = 10;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MidCircleView(Context context) {
        this(context, null);
    }

    public MidCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MidCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        cache = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels, Bitmap.Config.ARGB_8888);

        cacheCanvas = new Canvas(cache);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int cX = (int) event.getX();
                int cY = (int) event.getY();

                int dx = cX - downX;
                int dy = cY - downY;

                int distance = (int) Math.sqrt(dx * dx + dy * dy);

                r = distance / scale;

                drawX = downX / scale;
                drawY = downY / scale;

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;

        }

        invalidate();

        return true;
    }

    private Point[] drawMidCircle(int centerX, int centerY, Canvas canvas, Paint paint) {

        ArrayList<Point> cache = new ArrayList<>();

        int x, y;

        double d;

        x = 0;
        y = r;
//        d = (float) 1.25 - r;
        d = 0;

//        int upperAlpha = (int) (255 - Math.abs(d) / (float) r * 255.0F) << 24;
//        int lowerAlpha = 255 - upperAlpha;

        int lowerAlpha = ((int) (255 * (1 - DC(r, x)))) << 24;
        int upperAlpha = ((int) (255 * DC(r, x))) << 24;

        int tx = x;
        int ty = y - 1;

        paint.setColor(lowerAlpha | (paint.getColor() & 0xFFFFFF));

        canvas.drawRect(x + centerX, y + centerY, x + 1 + centerX, y + 1 + centerY, paint);

        cache.add(new Point(x + centerX, y + centerY));

        while (x < y) {

            Log.d("d:" + d, "x:" + x + ",y:" + y);
            Log.e("lowerAlpha" + lowerAlpha, "upperAlpha" + upperAlpha);

            paint.setColor(lowerAlpha | (paint.getColor() & 0xFFFFFF));

            canvas.drawRect(y + centerX, x + centerY, y + 1 + centerX, x + 1 + centerY, paint);
            canvas.drawRect(y + centerX, -x + centerY, y + 1 + centerX, -x + 1 + centerY, paint);
            canvas.drawRect(x + centerX, -y + centerY, x + 1 + centerX, -y + 1 + centerY, paint);
            canvas.drawRect(-x + centerX, -y + centerY, -x + 1 + centerX, -y + 1 + centerY, paint);
            canvas.drawRect(-y + centerX, -x + centerY, -y + 1 + centerX, -x + 1 + centerY, paint);
            canvas.drawRect(-y + centerX, x + centerY, -y + 1 + centerX, x + 1 + centerY, paint);
            canvas.drawRect(-x + centerX, y + centerY, -x + 1 + centerX, y + 1 + centerY, paint);

            paint.setColor(upperAlpha | (paint.getColor() & 0xFFFFFF));

            canvas.drawRect(ty + centerX, tx + centerY, ty + 1 + centerX, tx + 1 + centerY, paint);
            canvas.drawRect(ty + centerX, -tx + centerY, ty + 1 + centerX, -tx + 1 + centerY, paint);
            canvas.drawRect(tx + centerX, -ty + centerY, tx + 1 + centerX, -ty + 1 + centerY, paint);
            canvas.drawRect(-tx + centerX, -ty + centerY, -tx + 1 + centerX, -ty + 1 + centerY, paint);
            canvas.drawRect(-ty + centerX, -tx + centerY, -ty + 1 + centerX, -tx + 1 + centerY, paint);
            canvas.drawRect(-ty + centerX, tx + centerY, -ty + 1 + centerX, tx + 1 + centerY, paint);
            canvas.drawRect(-tx + centerX, ty + centerY, -tx + 1 + centerX, ty + 1 + centerY, paint);

            cache.add(new Point(y + centerX, x + centerY));
            cache.add(new Point(y + centerX, -x + centerY));
            cache.add(new Point(x + centerX, -y + centerY));
            cache.add(new Point(-x + centerX, -y + centerY));
            cache.add(new Point(-y + centerX, -x + centerY));
            cache.add(new Point(-y + centerX, x + centerY));
            cache.add(new Point(-x + centerX, y + centerY));

//            if (d < 0) {
//                d += 2 * x + 3;
//            } else {
//                d += 2 * (x - y) + 5;
//                y--;
//            }

            x++;
            if(DC(r, x) < d){
                y--;
            }



//            upperAlpha = (int) (255 - Math.abs(d) / (float) r * 255.0F) << 24;
//            lowerAlpha = 255 - upperAlpha;

            lowerAlpha = ((int) (255 * (1 - DC(r, x)))) << 24;
            upperAlpha = ((int) (255 * DC(r, x))) << 24;

            paint.setColor(lowerAlpha | (paint.getColor() & 0xFFFFFF));

            canvas.drawRect(x + centerX, y + centerY, x + 1 + centerX, y + 1 + centerY, paint);

            paint.setColor(upperAlpha | (paint.getColor() & 0xFFFFFF));

            tx = x;
            ty = y - 1;

            canvas.drawRect(tx + centerX, ty + centerY, tx + 1 + centerX, ty + 1 + centerY, paint);

            cache.add(new Point(x + centerX, y + centerY));

            d =  DC(r, x);

        }

        return cache.toArray(new Point[cache.size()]);
    }

    @Override
    public void onDraw(Canvas canvas) {

        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(1);

        cacheCanvas.drawRect(0, 0, cache.getWidth(), cache.getHeight(), paint);

        paint.setColor(0xFF0099CC);
        paint.setStyle(Paint.Style.STROKE);

        cacheCanvas.drawCircle(drawX, drawY + 30, r, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.scale(scale, scale);
        canvas.drawBitmap(cache, 0, 0, paint);
        drawMidCircle(drawX, drawY, canvas, paint);



    }

    double DC(int r, int y){
        double x = Math.sqrt(r * r - y * y);
        return Math.ceil(x) - x;
    }

}
