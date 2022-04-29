package com.hcdc.cc106_finalproject_montera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

// Author: John Henly Montera
// BSIT 3rd-Year
// April 26 ,2022

public class MainActivity extends AppCompatActivity {

    private ToggleButton stopandgo;
    private Button endsessionmain;
    private TextView steps,distance,calburned,timerText;
    private CircularProgressBar steprog;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    private double distancecount = 0,calcount = 0;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;
    private Integer goal_distance = 0;
    SQLiteDatabase myDB;
    private String extractedid;
    private Double mainkg;
    private int mainsession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Distance Goal!");
        alert.setMessage("Message : Enter your today's session distance goal!");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int kmtosteps = Integer.parseInt(input.getText().toString());
                Double converter = kmtosteps * 1312.3359580083;
                goal_distance = converter.intValue();
                return;
            }
        });
        alert.show();

        steps = findViewById(R.id.steps); //
        steprog = findViewById(R.id.steprog);
        distance = findViewById(R.id.distance); //
        calburned = findViewById(R.id.calburned); //
        stopandgo = findViewById(R.id.stopandgo);
        timerText = findViewById(R.id.timerText); //
        endsessionmain = findViewById(R.id.endsession);

        timer = new Timer();

        steprog.setProgressMax(goal_distance);
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

        endsessionmain.setOnClickListener(view -> {
            Intent myname = getIntent();
            String name = myname.getStringExtra("name");

            myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);
            Cursor acc_count = myDB.rawQuery("SELECT user_id AS user_id FROM useracc where useracc.name = ?;", new String[] {name});
            while (acc_count.moveToNext()){
                int countindex = acc_count.getColumnIndex("user_id");
                extractedid = acc_count.getString(countindex);
            }
            myDB.close();

            myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);
            ContentValues cv = new ContentValues();
            cv.put("user_id", extractedid);
            cv.put("calories_burned", calburned.getText().toString());
            cv.put("steps_count", steps.getText().toString());
            cv.put("distance", distance.getText().toString());
            cv.put("created_at", String.valueOf(Calendar.getInstance().getTime()));
            cv.put("duration", timerText.getText().toString());
            myDB.insert("usersessionstats ", null, cv);
            myDB.close();

            myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);
            Cursor extrac_kg_session = myDB.rawQuery("SELECT user_kg,count_sessions FROM useracc where useracc.name = ? AND user_id =?;", new String[] {name,extractedid});
            while (extrac_kg_session.moveToNext()){
                int countkg = extrac_kg_session.getColumnIndex("user_kg");
                int countsession = extrac_kg_session.getColumnIndex("count_sessions");
                String testkg = extrac_kg_session.getString(countkg);
                String testsession = extrac_kg_session.getString(countsession);
                mainkg = Double.parseDouble(testkg);
                mainsession = Integer.parseInt(testsession);
            }
            myDB.close();

            myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);
            ContentValues cv2 = new ContentValues();
            mainkg = mainkg - (Double.parseDouble(calburned.getText().toString()) * 0.00013);
            mainsession = mainsession + 1;
            cv2.put("user_kg", mainkg);
            cv2.put("count_sessions", mainsession);
            myDB.update("useracc", cv2, "user_id ="+extractedid, null);
            myDB.close();

            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Session Finished!", Toast.LENGTH_SHORT).show();
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