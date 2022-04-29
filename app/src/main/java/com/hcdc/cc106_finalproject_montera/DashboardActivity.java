package com.hcdc.cc106_finalproject_montera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    ArrayList<UserStats_Model> userstatsmodel;
    RecyclerView rviewmain;
    TextView accidmain;
    SQLiteDatabase myDB;
    String name,extractedid;
    Button addsessionmain;
    int countindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ///////////////////////////////////////////////////////////////////////////
        addsessionmain = findViewById(R.id.addsession);
        accidmain = findViewById(R.id.accid);
        rviewmain = findViewById(R.id.rview);
        ///////////////////////////////////////////////////////////////////////////
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        accidmain.setText(name);
        ///////////////////////////////////////////////////////////////////////////
        myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);
        Cursor extractid = myDB.rawQuery("SELECT user_id AS user_id FROM useracc where useracc.name = ?;", new String[] {name});
        while (extractid.moveToNext()){
            countindex = extractid.getColumnIndex("user_id");
            extractedid = extractid.getString(countindex);
        }
        extractid.close();
        myDB.close();
        ///////////////////////////////////////////////////////////////////////////
        userstatsmodel = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        rviewmain.setLayoutManager(layoutManager);
        myDB = openOrCreateDatabase("cc106_pedometer.db", 0, null);
        Cursor cursor = myDB.query("usersessionstats", null, null, null, null, null, "session_id");
        while (cursor.moveToNext()){
            if (cursor.getString(0).equals(extractedid)) {
                userstatsmodel.add(
                        new UserStats_Model(
                                Integer.parseInt(cursor.getString(0)),
                                Integer.parseInt(cursor.getString(1)),
                                Integer.parseInt(cursor.getString(2)),
                                Integer.parseInt(cursor.getString(3)),
                                Integer.parseInt(cursor.getString(4)),
                                cursor.getString(5),
                                cursor.getString(6)
                        )
                );
            }
        }
        cursor.close();
        myDB.close();
        rviewmain.setAdapter(new DataAdapter(userstatsmodel,this));

        ///////////////////////////////////////////////////////////////////////////
        addsessionmain.setOnClickListener(view ->{
            try{
                Intent addsession = new Intent(DashboardActivity.this, MainActivity.class);
                addsession.putExtra("name", name);
                startActivity(addsession);
            } catch (Exception e){
                Toast.makeText(this, "Error Occurred! Please Try Again Later ", Toast.LENGTH_SHORT).show();
            }
        });
        ///////////////////////////////////////////////////////////////////////////

    }
}