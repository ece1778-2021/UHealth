package com.example.uhealth;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class MedicationAdder extends AppCompatActivity {
   Button m_adder ;//= findViewById(R.id.btnAddMedication);


   EditText storageview;// = findViewById(R.id.medication_storage);
     EditText intervalview;// = findViewById(R.id.medication_interval);
     EditText medicineview ;//= findViewById(R.id.medicine_name);
    EditText repeats_counter ;//= findViewById(R.id.repeatscount);
    TextView start_repeat ;//= findViewById(R.id.startofRepeat);
    Button btnInitTime;//findViewById(R.id.btnPickInitTime);
    EditText dosisview;//=findViewById(R.id.dosis);

    private FireBaseInfo mFireBaseInfo;


    public void medicationListener(){
        //final Date currentDate = new Date();
        SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final Date currentDate = new Date();
        String current_time_text = mdateformat.format(currentDate);
        String medicine = medicineview.getText().toString();
        String interval = intervalview.getText().toString();
        String storage	= storageview.getText().toString();
        String dosis = dosisview.getText().toString();
        //----re adding username/uid to medication list
        String username = "Default Name";
        String userid = mFireBaseInfo.mUser.getUid();
        String init_of_repeat = start_repeat.getText().toString();
        String repeat_counts = repeats_counter.getText().toString();//actually inieger
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
        intent.putExtra("username",username);
        intent.putExtra("uid",userid );
        intent.putExtra("initdate",current_time_text);
        intent.putExtra("medicine",medicine );
        intent.putExtra("initstorage",storage);
        intent.putExtra("interval",interval );
        intent.putExtra("repeats",init_of_repeat);
        intent.putExtra("interval",repeat_counts );
        intent.putExtra("dosis",dosis );
        setResult(RESULT_OK, intent);
        finish();



    }
    public void initialMedicationTime(){

        final Calendar calendar = Calendar.getInstance();
        final List<String> selected_time = new ArrayList<String>();
        int[] timelist=new int[5];
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final String init_time = "1970-01-01-23:59";
        selected_time.add("1970-01-01-23:59");
        //String selected_time="1970-01-01-23:59";


        final TimePickerDialog timePickerDialog = new TimePickerDialog(MedicationAdder.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timelist[3] = hourOfDay;
                timelist[4] = minute;
                selected_time.remove(selected_time.get(0));
                String timetoAdd = String.format("%04d-%02d-%02d-%02d:%02d",timelist[0],timelist[1],timelist[2],timelist[3],timelist[4]);
                selected_time.add(0,timetoAdd);
                start_repeat.setText(timetoAdd);

            }
        }, hour, minute, true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MedicationAdder.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                timelist[0] = year;
                timelist[1] = monthOfYear + 1;
                timelist[2]= dayOfMonth;
                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();
    }
    public void init_views(){
        m_adder= findViewById(R.id.btnAddMedication);


        storageview= findViewById(R.id.medication_storage);
        intervalview = findViewById(R.id.medication_interval);
        medicineview = findViewById(R.id.medicine_name);
        repeats_counter = findViewById(R.id.repeatscount);
       start_repeat = findViewById(R.id.startofRepeat);
       btnInitTime=findViewById(R.id.btnPickInitTime);
        dosisview=findViewById(R.id.dosis);
        btnInitTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                initialMedicationTime();
            }
        });
        m_adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicationListener();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_adder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init_views();
        mFireBaseInfo =new FireBaseInfo();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
