package com.hcdc.cc106_finalproject_montera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

// Author: John Henly Montera
// BSIT 3rd-Year
// April 26 ,2022

public class MainActivity extends AppCompatActivity {

    private ToggleButton stopandgo;
    private TextView steps,distance,calburned,timerText;
    private CircularProgressBar steprog;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    private double distancecount = 0,calcount = 0;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        steps = findViewById(R.id.steps);
        steprog = findViewById(R.id.steprog);
        distance = findViewById(R.id.distance);
        calburned = findViewById(R.id.calburned);
        stopandgo = findViewById(R.id.stopandgo);
        timerText = findViewById(R.id.timerText);

        timer = new Timer();

        steprog.setProgressMax(200f); // i initialize pasa user kung pila ka km iyang gusto idagan
        steprog.setProgressBarColor(Color.BLACK);
        steprog.setProgressBarColorStart(Color.GRAY);
        steprog.setProgressBarColorEnd(Color.GREEN);
        steprog.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);
        steprog.setBackgroundProgressBarColor(Color.GRAY);
        steprog.setBackgroundProgressBarColorStart(Color.RED);
        steprog.setBackgroundProgressBarColorEnd(Color.RED);
        steprog.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.BOTTOM_TO_END );
        steprog.setProgressBarWidth(15f); // in DP
        steprog.setBackgroundProgressBarWidth(15f); // in DP
        steprog.setRoundBorder(true);
        steprog.setStartAngle(180f);
        steprog.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null){
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if (MagnitudeDelta > 11){
                        stepCount++;
                        distancecount = stepCount*0.000762;
                        calcount = stepCount*0.04;
                    }
                    df.setRoundingMode(RoundingMode.DOWN);
                    steps.setText(stepCount.toString());
                    steprog.setProgress(stepCount);
                    distance.setText(df.format(distancecount)+" Km");
                    calburned.setText(df.format(calcount)+" Cal");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        stopandgo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sensorManager.registerListener(stepDetector, sensor,SensorManager.SENSOR_DELAY_NORMAL);
                    startTimer();
                } else {
                    sensorManager.unregisterListener(stepDetector,sensor);
                    timerTask.cancel();
                }
            }
        });

    }

    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onResume() {

        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
    }
}