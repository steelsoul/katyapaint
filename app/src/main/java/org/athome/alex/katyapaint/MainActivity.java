package org.athome.alex.katyapaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String TAG = "[KP:Main]";
    DrawView drawView;
    PaintTools paintTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "ctor");

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(linearLayout, linearLayoutParams);

        paintTools = new PaintTools(this);
        paintTools.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));
        paintTools.setBackgroundResource(R.drawable.back);
        linearLayout.addView(paintTools);

        drawView = new DrawView(this);
        drawView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        drawView.setBackgroundResource(R.drawable.back);
        linearLayout.addView(drawView);
    }

    class PaintTools extends  View {
        final String LOG_TAG = "[KP:PaintTools]";

        public PaintTools(Context context) {
            super(context);
            Log.i(LOG_TAG, "ctor");
        }
    }

    class DrawView extends View implements View.OnTouchListener {
        final String TAG = "[KP:DrawView]";
        Paint paint;
        Path path;
        Canvas canvas;
        Bitmap bitmap;
        Rect dest;


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
            bitmap = null;
            canvas = null;
            dest = new Rect();
            setOnTouchListener(this);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            Log.d(TAG, "onSizeChanged: " + w + ", " + h);
            bitmap = Bitmap.createBitmap(w-8, h-8, Bitmap.Config.RGB_565);
            canvas = new Canvas(bitmap);
            canvas.drawARGB(255, 255, 255, 255);
            dest.set(4, 4, w-4, h-4);
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
            if (bitmap != null) canvas.drawBitmap(bitmap, null, dest, paint);
        }
    }
}
