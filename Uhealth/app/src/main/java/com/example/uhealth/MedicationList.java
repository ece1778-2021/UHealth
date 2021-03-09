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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MedicationList extends AppCompatActivity {
    private FireBaseInfo mFireBaseInfo;
    List<Medication> MedicationList = new ArrayList<>();
    MedicationAdapter medicationAdapter;
    final static int REQUEST_ADD_A_MEDICATION = 200;
    public void start_adder(){
        Intent addMedIntent = new Intent(this,MedicationAdder.class);
        startActivityForResult(addMedIntent, REQUEST_ADD_A_MEDICATION);

    }

    public void bubble(Medication newinstance){
        final Date currentDate = new Date();
        SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String init_time = newinstance.getInitDate();
        Long newtime =new  Long(0);
        Long oldtime =new Long(0);

        if(MedicationList.size()<=0){
            MedicationList.add(newinstance);
        }
        else{
            for(int i =0; i <= MedicationList.size();i++){
                if(i==MedicationList.size())
                {
                    MedicationList.add(i,newinstance);
                }else{
                    int res = 0;
                    try{
                        newtime = mdateformat.parse(init_time ).getTime();
                    }
                    catch(Exception e){

                    }
                    try{
                        oldtime = mdateformat.parse(MedicationList.get(i).getInitDate()).getTime();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(newtime<oldtime){

                    }
                    else{
                        MedicationList.add(i,newinstance);
                        break;
                    }
                }


            }

        }

    }
    public void downloadMedicationList(){
        String muid = mFireBaseInfo.mUser.getUid();
        //HashMap<String,Object>
        mFireBaseInfo.mFirestore.collection("Medication").whereEqualTo("uid",muid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int istorage=20;
                int iinterval = 12;
                if (task.isSuccessful()) {

                    QuerySnapshot query_res =  task.getResult();
                    List <DocumentSnapshot> documents = query_res.getDocuments();

                    for(int i =0; i < documents.size();i++){
                        HashMap<String,Object> resMap=  new HashMap<String,Object>();
                        resMap.put("medicine",documents.get(i).getString("medicine"));
                        resMap.put("initdate",documents.get(i).getString("initdate"));
                        resMap.put("username",documents.get(i).getString("username"));
                        resMap.put("uid",documents.get(i).getString("uid"));
                        try{
                            istorage=Integer.parseInt(documents.get(i).getString("initstorage"));

                        }catch(NumberFormatException e){
                            e.printStackTrace();
                        }
                        try{
                            iinterval=Integer.parseInt(documents.get(i).getString("interval"));

                        }catch(NumberFormatException e){
                            e.printStackTrace();
                        }
                        resMap.put("initstorage",istorage);
                        resMap.put("interval",iinterval);
                        Medication medication_to_download= new Medication(resMap);

                        bubble(medication_to_download);

                    }
                    medicationAdapter.notifyDataSetChanged();


                } else {
                    // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        int istorage=20;
        int iinterval = 12;
        switch (requestCode) {

            case REQUEST_ADD_A_MEDICATION  :{
                if (resultCode == RESULT_OK) {

                    HashMap<String,Object> resMap = new HashMap<String,Object>();
                    resMap.put("medicine",data.getStringExtra("medicine"));
                    resMap.put("initdate",data.getStringExtra("initdate"));
                    resMap.put("username",data.getStringExtra("username"));
                    resMap.put("uid",data.getStringExtra("uid"));
                    try{
                        istorage=Integer.parseInt(data.getStringExtra("initstorage"));

                    }catch(NumberFormatException e){
                        e.printStackTrace();
                    }
                    try{
                        iinterval=Integer.parseInt(data.getStringExtra("interval"));

                    }catch(NumberFormatException e){
                        e.printStackTrace();
                    }
                    resMap.put("initstorage",istorage);
                    resMap.put("interval",iinterval);


                    Medication temp_appointment = new Medication(resMap);
                    bubble(temp_appointment);
                    medicationAdapter.notifyDataSetChanged();

                }
            }




            default:{

            }

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);


        mFireBaseInfo = new FireBaseInfo();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_medication);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        medicationAdapter = new MedicationAdapter(MedicationList);
        recyclerView.setAdapter(medicationAdapter );
        downloadMedicationList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_adder();
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });
    }

}
