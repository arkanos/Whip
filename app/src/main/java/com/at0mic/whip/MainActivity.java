package com.at0mic.whip;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.io.IOException;

import static android.content.Context.CONTEXT_INCLUDE_CODE;


public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MediaPlayer mPlayer;
    private final static float ACC = 18;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whip);
        try {
            mPlayer.prepare();
            mPlayer.setVolume(1,1);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        ImageAdapter adapter = new ImageAdapter(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0: mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whip);
                        break;
                    case 1: mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tata);
                        break;
                    case 2: mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bee);
                        break;
                }

                try {
                    mPlayer.prepare();
                    mPlayer.setVolume(1,1);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else{
            setContentView(R.layout.activity_nosensor);
        }
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // So... we don't do anything here. :D
    }

    public final void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();
        float[] values = event.values;

        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            if ((Math.abs(values[0]) > ACC || Math.abs(values[1]) > ACC || Math.abs(values[2]) > ACC)) {
                Log.i("sensor", "running");
                mPlayer.start();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
