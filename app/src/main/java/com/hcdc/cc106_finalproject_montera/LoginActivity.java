package com.hcdc.cc106_finalproject_montera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    SQLiteDatabase myDB;
    EditText emailmain,passwordmain;
    Button loginbutton,registerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailmain = findViewById(R.id.email);
        passwordmain = findViewById(R.id.password);
        loginbutton = findViewById(R.id.login);
        registerbutton = findViewById(R.id.register);

        myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);

        myDB.execSQL("create table if not exists useracc (" +
                "user_id integer primary key autoincrement," +
                "name varchar(255) UNIQUE," +
                "status varchar(255)," +
                "password varchar(255)," +
                "email varchar(255) UNIQUE" +
                ")");

        myDB.execSQL("create table if not exists userstats (" +
                "userstats_id integer primary key autoincrement," +
                "user_id integer," +
                "user_kg integer," +
                "count_sessions integer" +
                ")");

        myDB.execSQL("create table if not exists usersessionstats (" +
                "session_id integer primary key autoincrement," +
                "user_id integer," +
                "calories_burned integer," +
                "steps_count integer," +
                "distance integer," +
                "created_at DATETIME" +
                ")");

        myDB.close();

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor acc_count = myDB.rawQuery("SELECT COUNT(*) FROM useracc where useracc.email = ? AND useracc.password = ?;", new String[] {emailmain.getText().toString(),passwordmain.getText().toString()});
                if (acc_count != null){
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials! Please Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


}