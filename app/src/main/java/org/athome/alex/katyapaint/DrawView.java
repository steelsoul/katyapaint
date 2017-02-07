package org.athome.alex.katyapaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Provides paint field able to react on user input
 * @author Alexander Pushkar
 * @version 1.0
 * @since 2016-01-29
 */

class DrawView extends View implements View.OnTouchListener {
    final String LOG_TAG = "[KP:DrawView]";
    Paint paint;
    Path path;
    Canvas canvas;
    Bitmap bitmap;
    Rect dest;
    boolean erase = false;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(LOG_TAG, "ctor");
        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        path = new Path();
        bitmap = null;
        canvas = null;
        dest = new Rect();
        setOnTouchListener(this);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(LOG_TAG, "onSizeChanged: " + w + ", " + h);
        bitmap = Bitmap.createBitmap(w-8, h-8, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        canvas.drawARGB(255, 255, 255, 255);
        dest.set(4, 4, w-4, h-4);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i(LOG_TAG, "onTouch ACTION_DOWN");
            path.reset();
            path.moveTo(event.getX(), event.getY());
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path.lineTo(event.getX(), event.getY());
            canvas.drawPath(path, paint);
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(LOG_TAG, "onTouch ACTION_UP");
            canvas.drawPath(path, paint);
            invalidate();
            return true;
        }
        Log.d(LOG_TAG, "onTouch event fired action: " + MotionEvent.actionToString(event.getAction()));
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) canvas.drawBitmap(bitmap, null, dest, paint);
    }

    public void setColor(int color){
        erase = false;
        paint.setColor(color);
        paint.setStrokeWidth(3);
    }

    public void eraserOn() {
        erase = true;
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(30);
    }
}
