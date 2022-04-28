package com.hcdc.cc106_finalproject_montera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    SQLiteDatabase myDB;
    EditText name, emailregistermain, passwordregistermain, confirmpassword;
    Button createuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        emailregistermain = findViewById(R.id.emailregister);
        passwordregistermain = findViewById(R.id.passwordregister);
        confirmpassword = findViewById(R.id.confirmpassword);
        createuser = findViewById(R.id.createuser);

        createuser.setOnClickListener(view -> {
            if (passwordregistermain.getText().toString().equals(confirmpassword.getText().toString())){
                register(emailregistermain.getText().toString(),passwordregistermain.getText().toString(),name.getText().toString());
            }
            else{
                Toast.makeText(this, "Your Password and Confirm Password are not the same", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void register(String email, String password, String name){

    try {
        myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", password);
        cv.put("password", email);
        myDB.insert("useracc", null, cv);
        myDB.close();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(RegisterActivity.this, "Register Successfully!", Toast.LENGTH_SHORT).show();
    } catch (Exception e){
        Toast.makeText(this, "Error Occurred! Please try again later.", Toast.LENGTH_SHORT).show();
    }

    }
}