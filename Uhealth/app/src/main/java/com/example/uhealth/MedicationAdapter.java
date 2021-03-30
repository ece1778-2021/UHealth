package com.example.uhealth;

import androidx.appcompat.app.AlertDialog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {
    private List<Medication> mMedicationList;
    private FireBaseInfo mFireBaseInfo;
    private AlertDialog mUpdateMedicationDialog;
    private AlertDialog mAlarmStorageDialog;
    private AlertDialog mShowMedicationDialog;
    private List<String> mListStack = new ArrayList<String>();


    @NonNull
    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_unit,null);
        MedicationAdapter.ViewHolder holder=new MedicationAdapter.ViewHolder(view);
        return holder;
    }
    public MedicationAdapter(List<Medication> medicationList){
        mMedicationList = medicationList;
        mFireBaseInfo= new FireBaseInfo();
    }

    private List<String> showMedication(Medication mMedication){
        List<String> InfoList =new ArrayList<String>();
        InfoList.add(0, "Medicine:"+mMedication.getMedicine());
        InfoList.add(1,"Frequency: "+ mMedication.getInterval()+"hours");
        InfoList.add(2,"LastUpdate: "+ mMedication.getLastUpdate());
        InfoList.add(3,"NextUpdate: "+ mMedication.getNextUpdate());
        InfoList.add(4,"Dosis: "+ mMedication.getDosis()+"pills/tablets");
        //InfoList.add(2, "Location:  "+mMedication.getApptLocation());
        //InfoList.add(3, "Uid:       "+mMedication.getPatientID());
        return InfoList;

    }
    //setting
    public void startStorageReminder(View v,Medication mMedication){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
        alertBuilder.setTitle("Medicine storage reminder");
        LayoutInflater minflater = LayoutInflater.from(v.getContext());
        View em_view = minflater.inflate(R.layout.storage_updater, null);
        alertBuilder.setView(em_view);
        EditText StorageAdder = (EditText) em_view.findViewById(R.id.medication_storage_updater);
        StorageAdder.setText("0");

        alertBuilder.setPositiveButton("Add",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String str_difference = StorageAdder.getText().toString();
               final int int_difference = Integer.valueOf(str_difference);
                mFireBaseInfo.mFirestore.collection("MMedication").whereEqualTo("uid",mMedication.getUid())
                        .whereEqualTo("medicine",mMedication.getMedicine())//.whereEqualTo("initdate",mMedication.getInitDate())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){

                                    QuerySnapshot query_res = task.getResult();
                                    List<DocumentSnapshot> documents = query_res.getDocuments();

                                    DocumentSnapshot delDocument = documents.get(0);
                                    int newStorage = 5;
                                    try{
                                        newStorage = int_difference+ Integer.valueOf(delDocument.get("currentstorage").toString());
                                    }catch(NullPointerException e){
                                        e.printStackTrace();
                                    }

                                    if(delDocument!=null){
                                        DocumentReference delDocumentRef = delDocument.getReference();
                                        delDocumentRef.update("currentstorage",newStorage);


                                    }
                                    else{

                                    }
                                }
                                else{

                                }

                            }


                        });

                mAlarmStorageDialog.dismiss();
            }
        });
        alertBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mAlarmStorageDialog.dismiss();
            }
        });
        mAlarmStorageDialog = alertBuilder.create();
        mAlarmStorageDialog.show();
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.ViewHolder holder, int position) {


        Medication mMedication=mMedicationList.get(position);
        //note from res id to actual bitmap
        //holder.fruitImage.setImageResource(mphoto.getImageID());

        String medication_snippet = position+"  :"+mMedication.getMedicine()+" "+mMedication.getLastUpdate();
        holder.medicationTag.setText(medication_snippet);

        holder.medicationChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> Selectedmedication =  showMedication(mMedication);
                final String[] items =  Selectedmedication.toArray(new String[Selectedmedication.size()]);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Check medication");
                Toast.makeText(v.getContext(),mMedication.getrInitDate(),Toast.LENGTH_SHORT).show();
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                alertBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mShowMedicationDialog.dismiss();
                    }
                });
                mShowMedicationDialog = alertBuilder.create();
                mShowMedicationDialog.show();
            }
        });
        Date NextUpdate = new Date();
        NextUpdate.setTime((long)1000*mMedication.getintNextUpdate());
        Date CurrentDate = new Date();
        if(CurrentDate.getTime()-NextUpdate.getTime()> 0 ) {
            holder.medicationUpdater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] items = {"Finished", "Missed"};
                    final View vv = v;
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setTitle("Select your medication condition");
                    alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // mListStack.add(0, items[which]);
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mUpdateMedicationDialog.dismiss();
                        }
                    });
                    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListStack.add("Finished");
                            String selectedUpdate = mListStack.get(0);
                            final long OriginalStorage = mMedication.getCurrentStorage();
                            long newStorage;
                            if (selectedUpdate.equals("Finished")) {
                                newStorage = Math.max(0, mMedication.getCurrentStorage() - mMedication.getDosis());

                            } else {
                                newStorage = OriginalStorage;
                            }
                            final long NewStorage = newStorage;

                            final long threshhold = Math.max(OriginalStorage, 12 * mMedication.getDosis());


                            mFireBaseInfo.mFirestore.collection("MMedication").whereEqualTo("uid", mMedication.getUid())
                                    .whereEqualTo("medicine", mMedication.getMedicine())//.whereEqualTo("initdate",mMedication.getInitDate())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                QuerySnapshot query_res = task.getResult();
                                                List<DocumentSnapshot> documents = query_res.getDocuments();

                                                DocumentSnapshot delDocument = documents.get(0);
                                                if (delDocument != null) {
                                                    DocumentReference delDocumentRef = delDocument.getReference();

                                                    Calendar medicationcalendar = Calendar.getInstance();
                                                    int interval = 4;
                                                    SimpleDateFormat mdateformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                                                    try {//ANIMPORTANTNOTE
                                                        interval = Integer.valueOf(delDocument.get("interval").toString());
                                                        Date last_time = mdateformat.parse(delDocument.get("nextupdate").toString());
                                                        String last_time_text = mdateformat.format(medicationcalendar.getTime());// delDocument.get("nextupdate").toString();
                                                        Date next_time = mdateformat.parse(last_time_text);
                                                        ;
                                                        // medicationcalendar.setTime(next_time);
                                                        medicationcalendar.add(medicationcalendar.HOUR_OF_DAY, interval);
                                                        String next_update_text = mdateformat.format(medicationcalendar.getTime());

                                                        if (NewStorage > 0) {
                                                            delDocumentRef.update("lastupdate", last_time_text);
                                                            delDocumentRef.update("nextupdate", next_update_text);
                                                            delDocumentRef.update("currentstorage", NewStorage);

                                                        } else {
                                                            delDocumentRef.update("status", "past");
                                                            delDocumentRef.update("endtime", last_time_text);
                                                            delDocumentRef.update("lastupdate", last_time_text);
                                                            delDocumentRef.update("nextupdate", last_time_text);
                                                            delDocumentRef.update("currentstorage", NewStorage);

                                                        }


                                                        //mMedication.setNextUpdate(next_update_text);
                                                        //try this one:


                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    ;
                                                } else {
                                                    Log.d("AAASSSAAASSS", "AAASSSAAASSS");
                                                }
                                            } else {
                                                Log.d("SSSSAAAASSSSAAAA", "SSSSAAAASSSSAAAA");
                                            }

                                        }


                                    });
                            if (NewStorage < threshhold && NewStorage >= threshhold - mMedication.getDosis()) {
                                startStorageReminder(vv, mMedication);
                            } else {

                            }

                            mUpdateMedicationDialog.dismiss();

                        }
                    });
                    //alertBuilder.setPositiveButton(OK)
                    //---------------------
                    mUpdateMedicationDialog = alertBuilder.create();

                    mUpdateMedicationDialog.show();

                    //---------------------


                }
            });
        }else{
            holder.medicationUpdater.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public int getItemCount() {
        return mMedicationList.size();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView medicationTag;
        Button medicationChecker;
        Button medicationUpdater;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicationTag = itemView.findViewById(R.id.medication_text_unit);
            medicationChecker = itemView.findViewById(R.id.btnMedicationChecker);
            medicationUpdater = itemView.findViewById(R.id.btnMedicationUpdater);
        }
       }
}
