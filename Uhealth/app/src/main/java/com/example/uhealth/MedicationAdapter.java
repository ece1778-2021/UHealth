package com.example.uhealth;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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


        holder.medicationUpdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mMedication
                final String[] items = {"Finished", "Missed"};

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Select your medication condition");
                alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mListStack.add(0, items[i]);


                        //Toast.makeText(MainActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                });

                alertBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mShowMedicationDialog.dismiss();
                    }
                });
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selectedUpdate = mListStack.get(0);

                        mFireBaseInfo.mFirestore.collection("Medication").whereEqualTo("medicine", mMedication.getMedicine())
                                .whereEqualTo("uid", mMedication.getUid()).whereEqualTo("initdate", mMedication.getInitDate()).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            QuerySnapshot query_res = task.getResult();
                                            List<DocumentSnapshot> documents = query_res.getDocuments();

                                            DocumentSnapshot delDocument = documents.get(0);
                                            if (delDocument != null) {
                                                DocumentReference delDocumentRef = delDocument.getReference();

                                                Calendar medicationcalendar =Calendar.getInstance();
                                                int interval = 4;
                                                SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                                                try{//ANIMPORTANTNOTE
                                                    interval = Integer.valueOf(delDocument.get("interval").toString());
                                                    Date last_time = mdateformat.parse(delDocument.get("nextupdate").toString());
                                                    Date next_time = mdateformat.parse(delDocument.get("nextupdate").toString());;
                                                    medicationcalendar.setTime(next_time);
                                                    medicationcalendar.add(medicationcalendar.HOUR_OF_DAY,interval);
                                                    String next_update_text = mdateformat.format(medicationcalendar.getTime());
                                                    delDocumentRef.update("lastupdate", last_time);
                                                    delDocumentRef.update("lastupdate", next_update_text);

                                                }catch(ParseException e){
                                                    e.printStackTrace();
                                                };


                                            } else {

                                            }


                                        } else {
                                            // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        mUpdateMedicationDialog.dismiss();
                        try{
                            SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                            Date date_time = mdateformat.parse(mMedication.getNextUpdate());
                            final Date currentDate = new Date();

                            if(currentDate.getTime() - date_time.getTime() > 0){
                                mUpdateMedicationDialog.show();
                            }else{
                                // nothing happened
                            }
                        }catch(java.text.ParseException e){
                            e.printStackTrace();
                        }
                    }

                });

            }});
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
