package com.example.uhealth;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
public class MedicationAdder extends AppCompatActivity {
   Button m_adder ;//= findViewById(R.id.btnAddMedication);

   EditText storageview;// = findViewById(R.id.medication_storage);
     EditText intervalview;// = findViewById(R.id.medication_interval);
     EditText medicineview ;//= findViewById(R.id.medicine_name);

    public void medicationListener(){
        //final Date currentDate = new Date();
        SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final Date currentDate = new Date();
        String current_time_text = mdateformat.format(currentDate);
        String medicine = medicineview.getText().toString();
        String interval = intervalview.getText().toString();
        String storage	= storageview.getText().toString();
        // String username = "demoName";
        // String userid = "123456";
	/*
        HashMap<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("date",appointment_time);
        resMap.put("physicainname",physician_name);
        resMap.put("patientid",userid);
        resMap.put("patientname",username);
        resMap.put("location",mLocation );
	*/

        Intent intent = new Intent();

        // intent.putExtra("data_return", resMap);
        intent.putExtra("initdate",current_time_text);
        intent.putExtra("medicine",medicine );
        intent.putExtra("initstorage",storage);
        intent.putExtra("interval",interval );
        setResult(RESULT_OK, intent);
        finish();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_adder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         m_adder= findViewById(R.id.btnAddMedication);

        storageview= findViewById(R.id.medication_storage);
         intervalview = findViewById(R.id.medication_interval);
         medicineview = findViewById(R.id.medicine_name);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        m_adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicationListener();
            }
        });
    }

}
