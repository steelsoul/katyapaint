package org.athome.alex.katyapaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    final String TAG = "[KP:Main]";
    DrawView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "ctor");
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(linearLayout, linearLayoutParams);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        drawView = new DrawView(this);
        drawView.setLayoutParams(layoutParams);
        linearLayout.addView(drawView);
    }

    class DrawView extends View implements View.OnTouchListener {
        final String TAG = "[KP:DrawView]";
        Paint paint;
        Path path;
        Canvas canvas;
        Bitmap bitmap;

        public DrawView(Context context) {
            super(context);
            Log.i(TAG, "ctor");
            paint = new Paint();
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);
            path = new Path();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.RGB_565);
            canvas = new Canvas(bitmap);

            setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i(TAG, "onTouch ACTION_DOWN");
                path.reset();
                path.moveTo(event.getX(), event.getY());
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                path.lineTo(event.getX(), event.getY());
                canvas.drawPath(path, paint);
                invalidate();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i(TAG, "onTouch ACTION_UP");
                canvas.drawPath(path, paint);
                invalidate();
                return true;
            }
            Log.d(TAG, "onTouch event fired action: " + MotionEvent.actionToString(event.getAction()));
            return false;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

    }
}
