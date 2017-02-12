package org.athome.alex.katyapaint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener {

    private static final String LOG_TAG = "[KP:Main]";
    private static final String COLOR_PREFERENCE_KEY = "color";
    DrawView drawView;
    SurfaceView surfaceView1;

    int[] color = {Color.BLACK, Color.WHITE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "ctor");

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawView = (DrawView) findViewById(R.id.drawView);
        surfaceView1 = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView1.getHolder().setFormat(PixelFormat.TRANSPARENT);
        surfaceView1.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                changeFGColor();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        surfaceView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateColorPicker(0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void colorChanged(int color, int index) {
        Log.d(LOG_TAG, "colorChanged: color [" + Integer.toHexString(color) + "] index: " + index);
        this.color[0] = color;
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(
                COLOR_PREFERENCE_KEY, color).apply();
        drawView.setColor(color);
        changeFGColor();
    }

    private void activateColorPicker(int index) {
        int color = PreferenceManager.getDefaultSharedPreferences(
                MainActivity.this).getInt(COLOR_PREFERENCE_KEY,
                Color.BLACK);
        new ColorPickerDialog(MainActivity.this, MainActivity.this, color, index).show();
    }

    public void eraserOn(View view) {
        Log.d(LOG_TAG, "eraserOn");
        drawView.eraserOn();
    }

    private void changeFGColor() {
        Log.d(LOG_TAG, "ChangeFGColor: " + Integer.toHexString(color[0]));
        Canvas preparedCanvas = surfaceView1.getHolder().lockCanvas();
        preparedCanvas.drawColor(color[0]);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.streetlighter);
        if (bm == null) {
            Log.d(LOG_TAG, "Bitmap is null");
        } else {
            preparedCanvas.drawBitmap(bm, null, new Rect(0, 0, preparedCanvas.getWidth(), preparedCanvas.getHeight()), null);
        }
        surfaceView1.getHolder().unlockCanvasAndPost(preparedCanvas);

        surfaceView1.draw(preparedCanvas);
    }
}
