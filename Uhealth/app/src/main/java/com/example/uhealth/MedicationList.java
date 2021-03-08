package com.example.uhealth;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MedicationList extends AppCompatActivity {
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



        RecyclerView recyclerView = findViewById(R.id.recycler_view_medication);
        medicationAdapter = new MedicationAdapter(MedicationList);
        recyclerView.setAdapter(medicationAdapter );

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
