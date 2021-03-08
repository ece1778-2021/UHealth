package com.example.uhealth;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AppointmentAdder extends AppCompatActivity {
    final Button m_adder = findViewById(R.id.btnAddAppointmnet);
    final Button time_option = findViewById(R.id.btnPickTime);
    final TextView timeboard = findViewById(R.id.selectedTime);
    final EditText doctornameview = findViewById(R.id.appointment_physcian_name);
    final EditText locationview = findViewById(R.id.appointment_location);
    final static int SET_APPOINTMENT_ALARM = 15;
    public void setAlarm(Date date_time){
        Intent intent = new Intent(AppointmentAdder.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(AppointmentAdder.this, SET_APPOINTMENT_ALARM , intent, 0);
        Calendar appointment_calendar  =Calendar.getInstance();
        appointment_calendar.setTime(date_time);
        appointment_calendar.add(appointment_calendar.DATE,-1);
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, appointment_calendar.getTimeInMillis(), sender);

    }
    public void appointmentListener(){
       //final Date currentDate = new Date();
       SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
       String appointment_time = timeboard.getText().toString();
       //String current_time_text = mdateformat.format(currentDate);
       String physician_name = doctornameview.getText().toString();
       String mLocation =  locationview.getText().toString();
       String username = "demoName";
       String userid = "123456";
      Date date_time = new Date();

        HashMap<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("date",appointment_time);
        resMap.put("physicainname",physician_name);
        resMap.put("patientid",userid);
        resMap.put("patientname",username);
        resMap.put("location",mLocation );


       Intent intent = new Intent();

      // intent.putExtra("data_return", resMap);
        intent.putExtra("date",appointment_time);
        intent.putExtra("physicainname",physician_name);
        intent.putExtra("patientid",userid);
        intent.putExtra("patientname",username);
        intent.putExtra("location",mLocation );
       setResult(RESULT_OK, intent);
       try{
           date_time = mdateformat.parse(appointment_time);
           setAlarm(date_time);
       }
       catch(ParseException e){
           e.printStackTrace();
       }




       finish();


       // SimpleDateFormat mdateformat=getDateTimeInstance("yyyy-MM-dd HH:mm")
        //"1970-01-01-23:59";
    }
    public void startTimePicker(){
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


        final TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentAdder.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timelist[3] = hourOfDay;
                timelist[4] = minute;
                selected_time.remove(selected_time.get(0));
                String timetoAdd = String.format("%04d-%02d-%02d-%02d:%02d",timelist[0],timelist[1],timelist[2],timelist[3],timelist[4]);
                selected_time.add(0,timetoAdd);
                timeboard.setText(timetoAdd);

            }
        }, hour, minute, true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentAdder.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                timelist[0] = year;
                timelist[1] = monthOfYear;
                timelist[2]= dayOfMonth;
                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();

    }

    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_adder);
        timeboard.setText("1970-01-01-23:59");
        time_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTimePicker();
            }
        });
        m_adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentListener();
            }
        });
    }
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });
        */

    }


