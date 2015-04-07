package com.sevenheaven.shzoomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by caifangmao on 15/4/7.
 */
public class ZoomLayout extends FrameLayout {

    private SHZoomView zoomView;

    private int drawX;
    private int drawY;
    private Rect bound = new Rect();

    private float rawX;
    private float rawY;

    private int zoomViewSize;
    private float zoomViewScale;
    private int zoomStrokeColor;

    public ZoomLayout(Context context){
        this(context, null);
    }

    public ZoomLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZoomLayout);

        zoomViewSize = ta.getDimensionPixelOffset(R.styleable.ZoomLayout_zoomSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics()));
        zoomViewScale = ta.getFloat(R.styleable.ZoomLayout_zoomScale, 3.0F);
        zoomStrokeColor = ta.getColor(R.styleable.ZoomLayout_strokeColor, 0xFF0099CC);


        ta.recycle();

        zoomView = new SHZoomView(context, attrs, defStyle);
        zoomView.setScale(zoomViewScale);
        zoomView.setStrokeColor(zoomStrokeColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                zoomView.attach(zoomViewSize);

                rawX = event.getRawX();
                rawY = event.getRawY();

                zoomView.updatePosition(rawX - zoomViewSize / 2, rawY - zoomViewSize - 100);
                zoomView.doDraw(rawX, rawY, this);
                break;
            case MotionEvent.ACTION_MOVE:

                rawX = event.getRawX();
                rawY = event.getRawY();

                zoomView.updatePosition(rawX - zoomViewSize / 2, rawY - zoomViewSize - 100);
                zoomView.doDraw(rawX, rawY, this);

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                zoomView.detach();
                break;
        }

        return true;
    }
}
