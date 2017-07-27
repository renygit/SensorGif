package com.renygit.myplay;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private GifImageView giv;
    private GifDrawable gifFromAssets;

    private SensorManager sm;
    private Sensor sr;
    private SensorGifHelper helper = new SensorGifHelper();

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        giv = (GifImageView) findViewById(R.id.giv);
        tv = (TextView) findViewById(R.id.tv);

        try {
            gifFromAssets = new GifDrawable(getAssets(), "anim.gif");
            gifFromAssets.stop();
            helper.setTotalFrames(gifFromAssets.getNumberOfFrames());
            giv.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sr = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            final int framesIndex = helper.getFramesBySensorX(event.values[0]);
            tv.setText("总帧数：" + gifFromAssets.getNumberOfFrames() +  "   当前帧："+ framesIndex);
            if((gifFromAssets.getCurrentFrameIndex() < framesIndex && gifFromAssets.canSeekBackward()) ||
                    (gifFromAssets.getCurrentFrameIndex() > framesIndex && gifFromAssets.canSeekForward())) {
                if (!gifFromAssets.isRunning()) {
                    sensorToFrame(framesIndex);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    public synchronized void sensorToFrame(final int framesIndex){
        ThreadUtils.self().add(new Runnable() {
            @Override
            public void run() {
                gifFromAssets.seekToFrame(framesIndex);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        sm.registerListener(sensorEventListener, sr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ThreadUtils.self().shutdown();
        sm.unregisterListener(sensorEventListener);
        if(!gifFromAssets.isRecycled()){
            gifFromAssets.recycle();
        }
    }
}
