package com.hcdc.cc106_finalproject_montera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    TextView accidmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        accidmain = findViewById(R.id.accid);
        Intent intent = getIntent();
        accidmain.setText(intent.getStringExtra("name"));


    }
}