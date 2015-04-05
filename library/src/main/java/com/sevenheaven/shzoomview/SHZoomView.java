package com.sevenheaven.shzoomview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by 7heaven on 15/4/5.
 */
public class SHZoomView extends View{
    private Bitmap zoomBitmap;
    private Canvas zoomCanvas;

    private Paint paint;

    private float x;
    private float y;

    private float layoutX;
    private float layoutY;

    private int gap;
    private int canvasSize;
    private int halfCanvasSize;

    private int width;
    private int height;

    private int centerX;
    private int centerY;

    private int screenWidth;
    private int screenHeight;

    private Path triPath;

    private float scale = 1.7F;
    private static final int arrowHeight = 10;

    private WindowManager windowManager;

    private PorterDuffXfermode xferMode;

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public SHZoomView(Context context){
        this(context, null);
    }

    public SHZoomView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public SHZoomView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        xferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        gap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        triPath = new Path();

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public void setupCanvas(int size){
        int doubleGap = gap * 2;

        this.canvasSize = size - doubleGap;
        this.halfCanvasSize = this.canvasSize / 2;

        zoomBitmap = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.RGB_565);
        zoomCanvas = new Canvas(zoomBitmap);
    }

    public void attach(int size){
        if(windowManager == null){
            windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        }

        setupCanvas(size);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(size, size);

        windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        windowParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowParams.format = PixelFormat.RGBA_8888;
        windowParams.x = 0;
        windowParams.y = 0;
        windowParams.width = size;
        windowParams.height = size;

        windowManager.addView(this, windowParams);
    }

    public void detach(){
        try{
            windowManager.removeView(this);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();

        detach();
    }

    public void updatePosition(float x, float y){
        this.layoutX = x;
        this.layoutY = y;

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();

        layoutParams.x = (int) x;
        layoutParams.y = (int) y;

        windowManager.updateViewLayout(this, layoutParams);
    }

    public void setScale(float scale){
        this.scale = scale;

    }

    public void doDraw(float x, float y, View view){
        this.x = x - halfCanvasSize;
        this.y = y - halfCanvasSize;

        if(zoomCanvas != null && zoomBitmap != null){
            zoomCanvas.save();
            zoomCanvas.translate(-this.x, -this.y);
            zoomCanvas.scale(scale, scale, this.x + halfCanvasSize, this.y + halfCanvasSize);
            if(view.getRootView().getDrawingCache() != null){
                zoomCanvas.drawBitmap(view.getRootView().getDrawingCache(), 0, 0, paint);
            }else{

                view.getRootView().draw(zoomCanvas);
            }
            zoomCanvas.restore();

            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas){

        if(zoomCanvas != null && zoomBitmap != null){

            canvas.save();

            if(this.layoutX < 0){
                canvas.translate(this.layoutX, 0);
            }else if(this.layoutX > screenWidth - getWidth()){
                canvas.translate(this.layoutX - (screenWidth - getWidth()), 0);
            }

            if(this.layoutY < 0){
                canvas.translate(0, this.layoutY);
            }else if(this.layoutY > screenHeight - getHeight()){
                canvas.translate(0, this.layoutY - (screenHeight - getHeight()));
            }

            paint.setColor(0xFF000000);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(centerX, halfCanvasSize + gap, halfCanvasSize, paint);

            canvas.drawPath(triPath, paint);

            paint.setXfermode(xferMode);

            canvas.drawBitmap(zoomBitmap, gap, gap, paint);

            paint.setXfermode(null);

            paint.setShadowLayer(5, 0, 5, 0x88000000);

            paint.setColor(0xFF0099CC);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            canvas.drawCircle(centerX, halfCanvasSize + gap, halfCanvasSize - 2, paint);


            paint.setShadowLayer(0, 0, 0, 0);

            canvas.restore();
        }
    }
}
