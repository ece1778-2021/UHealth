package com.example.uhealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MedicationList extends AppCompatActivity {
    List<Medication> MedicationList = new ArrayList<>();
   MedicationAdapter medicationAdapter;
    private FireBaseInfo mFireBaseInfo;
    Button MedicationRemover,MedicationAdder;
    private AlertDialog mRemoveMedicationDialog;
    private AlertDialog mAlarmMedicationDialog;
    final static int REQUEST_ADD_A_MEDICATION = 200;
    public void start_adder(){
        Intent addMedIntent = new Intent(this,MedicationAdder.class);
        startActivityForResult(addMedIntent, REQUEST_ADD_A_MEDICATION);

    }

    public void bubble(Medication newinstance){
        final Date currentDate = new Date();
        SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
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
    public void download_medication_list(){
        String uid = mFireBaseInfo.mUser.getUid();
        mFireBaseInfo.mFirestore.collection("Medication").whereEqualTo("uid",uid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {

                QuerySnapshot query_res =  task.getResult();
                List<DocumentSnapshot> documents = query_res.getDocuments();
                for(int i =0; i < documents.size();i++){
                    DocumentSnapshot instance = documents.get(i);
                    HashMap<String,Object> resMap = (HashMap)instance.getData();


                    Medication mmedication = new Medication(resMap);
                    bubble(mmedication );
                }
                medicationAdapter.notifyDataSetChanged();



            } else {
                // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
            }
        }
    });



    }
    public void upload_medication_instance(HashMap<String,Object> resMap){
        mFireBaseInfo.mFirestore.collection("Medication").add(resMap) .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
                });;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        int istorage=20;
        int iinterval = 12;
        int irepeatcounts = 3;
        int dosis = 1;
        switch (requestCode) {

            case REQUEST_ADD_A_MEDICATION  :{
                if (resultCode == RESULT_OK) {

                    HashMap<String,Object> resMap = new HashMap<String,Object>();
                    resMap.put("username",data.getStringExtra("username"));
                    resMap.put("uid",data.getStringExtra("uid"));
                    resMap.put("medicine",data.getStringExtra("medicine"));
                    resMap.put("initdate",data.getStringExtra("initdate"));
                    resMap.put("status",data.getStringExtra("status"));
                    resMap.put("lastupdate",data.getStringExtra("lastupdate"));
                    resMap.put("nextupdate",data.getStringExtra("nextupdate"));
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

                    try{
                        irepeatcounts =Integer.parseInt(data.getStringExtra("repeats"));

                    }catch(NumberFormatException e){
                        e.printStackTrace();
                    }

                    try{
                        dosis =Integer.parseInt(data.getStringExtra("dosis"));

                    }catch(NumberFormatException e){
                        e.printStackTrace();
                    }
                    resMap.put("dosis",dosis);
                    resMap.put("initstorage",istorage);
                    resMap.put("interval",iinterval);
                    resMap.put("repeats",irepeatcounts);

                    upload_medication_instance(resMap);
                    Medication temp_appointment = new Medication(resMap);
                    bubble(temp_appointment);
                    medicationAdapter.notifyDataSetChanged();

                }
            }




            default:{

            }

        }

    }
    int selectedInd=0;
    public void startRemover(){
        List<String> TitleList = new ArrayList<String>();
        String element;


        if(MedicationList.size()==0){
            TitleList.add("Empty medication list");
        }
        else{
            for (int ind =0; ind < MedicationList.size();ind++){
                Medication iterator = MedicationList.get(ind);
                element = ind+""+"Last Update:"+iterator.getLastUpdate();
                TitleList.add(element);
            }


        }



        final String[] items=TitleList.toArray(new String[TitleList.size()]);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Select an Medication to Cancel");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedInd=  i;
            }
        });

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // mirror image
                if(items[0].equals("Empty medication list")  ){


                }
                else{
                    // Log.d("aqaqaq",i+"");
                    Medication InstToDel = MedicationList.get(selectedInd);
                    //remote remove
                    remoteDelMedication(InstToDel);
                    remoteDelMedication(InstToDel);
                    //locally remove
                    MedicationList.remove(selectedInd);
                    medicationAdapter.notifyDataSetChanged();



                }
                mRemoveMedicationDialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mRemoveMedicationDialog.dismiss();
            }
        });
        mRemoveMedicationDialog = alertBuilder.create();
        mRemoveMedicationDialog.show();

    }
    public void remoteDelMedication(Medication instance){
        FireBaseInfo mFireBaseInfo = new FireBaseInfo();
        mFireBaseInfo.mFirestore.collection("Medication").whereEqualTo("uid",instance.getUid()).whereEqualTo("initdate",instance.getInitDate()).whereEqualTo("lastupdate",instance.getLastUpdate()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot query_res =  task.getResult();
                            List<DocumentSnapshot> documents = query_res.getDocuments();

                            DocumentSnapshot delDocument = documents.get(0);
                            if(delDocument!=null ){
                                DocumentReference delDocumentRef = delDocument.getReference();
                                delDocumentRef.update("status","Canceled");

                            }else{

                            }


                        } else {
                            // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void initListener(){
        MedicationRemover = findViewById(R.id.btnDelMedication);
        MedicationAdder = findViewById(R.id.btnAddMedication);;

        MedicationRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRemover();
            }
        });

        MedicationAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_adder();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);


        mFireBaseInfo =new FireBaseInfo();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_medication);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        medicationAdapter = new MedicationAdapter(MedicationList);
        recyclerView.setAdapter(medicationAdapter );
        download_medication_list();
        medicationAdapter.notifyDataSetChanged();
        initListener();
        fab.setVisibility(View.INVISIBLE);
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
