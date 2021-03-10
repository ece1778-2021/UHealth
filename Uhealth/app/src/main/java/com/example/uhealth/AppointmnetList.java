package com.example.uhealth;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppointmnetList extends AppCompatActivity {
    List<Appointment> AppointmentList = new ArrayList<>();
    AppointmentAdapter appointmentAdapter;
    private FireBaseInfo mFireBaseInfo;
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

    public void download_medication_list(){
        String uid = mFireBaseInfo.mUser.getUid();
        mFireBaseInfo.mFirestore.collection("Appointment").whereEqualTo("patientid",uid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot query_res =  task.getResult();
                            List<DocumentSnapshot> documents = query_res.getDocuments();
                            for(int i =0; i < documents.size();i++){
                                DocumentSnapshot instance = documents.get(i);
                                HashMap<String,Object> resMap =new HashMap<String,Object>();
                                /*
                     this.AppointmentType = Initializer.get("type").toString();
        this.ApptDate = Initializer.get("date").toString();
        //this.PhysicianID = Initializer.get("physicianid");
        this.PhysicianName = Initializer.get("physicainname").toString();
        this.PatientID = Initializer.get("patientid").toString();
        this.PatientName = Initializer.get("patientname").toString();
        this.ApptLocation = Initializer.get("location").toString();
                                 */


                                resMap.put("type",instance.get("type"));
                                resMap.put("date",instance.get("date"));
                                resMap.put("physicianid",instance.get("physicianid"));
                                resMap.put("physicainname",instance.get("physicainname"));
                                resMap.put("patientid",instance.get("patientid"));
                                resMap.put("patientname",instance.get("patientname") );
                                resMap.put("location",instance.get("location") );


                                Appointment mmedication = new Appointment(resMap);
                                bubble(mmedication );
                            }
                            appointmentAdapter.notifyDataSetChanged();



                        } else {
                            // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }
    public void upload_appointment_instance(HashMap<String,Object> resMap){
        mFireBaseInfo.mFirestore.collection("Appointment").add(resMap) .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Toast.makeText(RegisterActivity.this, "Congratulation!!!", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //  Toast.makeText(RegisterActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();

                    }

                    ;
                });

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
                    resMap.put("type",data.getStringExtra("type") );



                    Appointment temp_appointment = new Appointment(resMap);
                    bubble(temp_appointment);
                    appointmentAdapter.notifyDataSetChanged();
                   upload_appointment_instance(resMap);

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
        mFireBaseInfo = new FireBaseInfo();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        download_medication_list();
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
