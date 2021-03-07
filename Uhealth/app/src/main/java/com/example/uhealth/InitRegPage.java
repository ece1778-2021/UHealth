package com.example.uhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class InitRegPage extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_reg_page);
        toolbar = findViewById(R.id.initreg_toolbar);
        setSupportActionBar(toolbar);

    }
}