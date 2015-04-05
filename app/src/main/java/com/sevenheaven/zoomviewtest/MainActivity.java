package com.sevenheaven.zoomviewtest;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sevenheaven.shzoomview.SHZoomView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SHZoomView zoomView = new SHZoomView(this);
        zoomView.setScale(4.0F);

        ImageView view = new ImageView(this){

            int drawX;
            int drawY;
            Rect bound = new Rect();

            float rawX;
            float rawY;

            @Override
            public boolean onTouchEvent(MotionEvent event){

                switch(event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        zoomView.attach(300);

                        rawX = event.getRawX();
                        rawY = event.getRawY();

                        zoomView.updatePosition(rawX - zoomView.getWidth() / 2, rawY - zoomView.getHeight() - 100);
                        zoomView.doDraw(rawX, rawY, this);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        rawX = event.getRawX();
                        rawY = event.getRawY();

                        zoomView.updatePosition(rawX - zoomView.getWidth() / 2, rawY - zoomView.getHeight() - 100);
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
        };

        view.setImageResource(R.drawable.darth_vader);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view, layoutParams);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
