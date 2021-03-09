package com.example.uhealth;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppointmnetList extends AppCompatActivity {
    private FireBaseInfo mFireBaseInfo;
    List<Appointment> AppointmentList = new ArrayList<>();
    AppointmentAdapter appointmentAdapter;
    Button toAdder ; //findViewById(R.id.btnToAppointmentAdder);
    final static int REQUEST_ADD_AN_APPOINTMENT = 100;
    public void start_adder(){
        Intent addAptIntent = new Intent(this,AppointmentAdder.class);
        startActivityForResult(addAptIntent, REQUEST_ADD_AN_APPOINTMENT);
        //startActivity(addAptIntent);
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
                Toast.makeText(AppointmnetList.this,"Where are we???", Toast.LENGTH_LONG);
                if (resultCode == RESULT_OK) {
                    HashMap<String,Object> resMap = new HashMap<String,Object>();
                    resMap.put("date",data.getStringExtra("date"));
                    resMap.put("physicainname",data.getStringExtra("physicainname"));
                    resMap.put("patientid",data.getStringExtra("patientid"));
                    resMap.put("patientname",data.getStringExtra("patientname"));
                    resMap.put("location",data.getStringExtra("location") );

                    Appointment temp_appointment = new Appointment(resMap);
                    bubble(temp_appointment);
                    //AppointmentList.add(temp_appointment);
                    appointmentAdapter.notifyDataSetChanged();

                }
                break;
            }




            default:{
                Toast.makeText(AppointmnetList.this,"Where to set request code?", Toast.LENGTH_LONG);
            }

        }

    }
    @Override
    protected void onStart(){
        super.onStart();
        toAdder = findViewById(R.id.btnToAppointmentAdder);
        toAdder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                start_adder();
            }
        });

    }
    public void downloadAppointmentList(){

        String muid = mFireBaseInfo.mUser.getUid();
        //HashMap<String,Object>
        mFireBaseInfo.mFirestore.collection("Appointment").whereEqualTo("uid",muid)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot query_res =  task.getResult();
                   List <DocumentSnapshot> documents = query_res.getDocuments();

                   for(int i =0; i < documents.size();i++){
                       HashMap<String,Object> resMap=  new HashMap<String,Object>();
                       resMap.put("date",documents.get(i).getString("date"));
                       resMap.put("physicainname",documents.get(i).getString("physicainname"));
                       resMap.put("patientid",documents.get(i).getString("patientid"));
                       resMap.put("patientname",documents.get(i).getString("patientname"));
                       resMap.put("location",documents.get(i).getString("location"));
                       Appointment appointment_to_download= new Appointment(resMap);

                       bubble(appointment_to_download);

                   }
                    appointmentAdapter.notifyDataSetChanged();

                } else {
                    // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmnet_list);
        mFireBaseInfo = mFireBaseInfo = new FireBaseInfo();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_appointmnet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        appointmentAdapter = new AppointmentAdapter(AppointmentList);
        recyclerView.setAdapter(appointmentAdapter );
        downloadAppointmentList();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  start_adder();
             //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });
    }

}
