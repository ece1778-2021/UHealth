package com.example.uhealth;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppointmnetList extends AppCompatActivity {
    List<Appointment> AppointmentList = new ArrayList<>();
    AppointmentAdapter appointmentAdapter;
    final static int REQUEST_ADD_AN_APPOINTMENT = 100;
    public void start_adder(){
        Intent addAptIntent = new Intent(this,AppointmentAdder.class);
        startActivityForResult(addAptIntent, REQUEST_ADD_AN_APPOINTMENT);

    }
    public void bubble(Appointment newinstance){
        final Date currentDate = new Date();
        SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String appointment_time = newinstance.getDate();
        Long newtime =new  Long(0);
        Long oldtime =new Long(0);

        if(AppointmentList.size()<=0){
            AppointmentList.add(newinstance);
        }
        else{
            for(int i =0; i <= AppointmentList.size();i++){
                if(i==AppointmentList.size())
                {
                    AppointmentList.add(i,newinstance);
                }else{
                    int res = 0;
                    try{
                        newtime = mdateformat.parse(appointment_time).getTime();
                    }
                    catch(Exception e){

                    }
                    try{
                        oldtime = mdateformat.parse(AppointmentList.get(i).getDate()).getTime();
                    }catch(Exception e){

                    }
                    if(newtime<oldtime){

                    }
                    else{
                        AppointmentList.add(i,newinstance);
                        break;
                    }
                }


            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {

            case REQUEST_ADD_AN_APPOINTMENT :{
                if (resultCode == RESULT_OK) {
                    HashMap<String,Object> resMap = new HashMap<String,Object>();
                    resMap.put("date",data.getStringExtra("date"));
                    resMap.put("physicainname",data.getStringExtra("physicainname"));
                    resMap.put("patientid",data.getStringExtra("patientid"));
                    resMap.put("patientname",data.getStringExtra("patientname"));
                    resMap.put("location",data.getStringExtra("location") );

                    Appointment temp_appointment = new Appointment(resMap);
                    bubble(temp_appointment);
                    appointmentAdapter.notifyDataSetChanged();

                }
            }




            default:{

            }

        }

    }
    @Override
    protected void onStart(){
        super.onStart();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmnet_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_appointmnet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        appointmentAdapter = new AppointmentAdapter(AppointmentList);
        recyclerView.setAdapter(appointmentAdapter );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_adder();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });
    }

}
