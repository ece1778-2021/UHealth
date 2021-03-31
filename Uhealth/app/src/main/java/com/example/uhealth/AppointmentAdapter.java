package com.example.uhealth;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhealth.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<Appointment> mAppointmentList;
    private FireBaseInfo mFireBaseInfo;
    private AlertDialog mShowAppointmentDialog;
    private AlertDialog mRemoveAppointmentDialog;
    private AlertDialog mUpdateAppointmentDialog;
    private AlertDialog mFurtherUpdateDialog;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_unit,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    public AppointmentAdapter(List<Appointment> appointmentList){
        mAppointmentList=appointmentList;
        mFireBaseInfo= new FireBaseInfo();
    }
    private List<String> showAppointment(Appointment mAppointment){
        List<String> InfoList =new ArrayList<String>();
        InfoList.add(0, "Time:      "+mAppointment.getDate());
        InfoList.add(1,"Type:       "+ mAppointment.getAppointmentType());
        InfoList.add(2, "Location:      "+mAppointment.getApptLocation());
        InfoList.add(3, "Uid:       "+mAppointment.getPatientID());
        return InfoList;

    }
    public void furtherUpdate(View v,Appointment mAppointment){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());

        LayoutInflater minflater = LayoutInflater.from(v.getContext());
        View em_view = minflater.inflate(R.layout.appointment_update_dialog, null);
        EditText EdNote = (EditText) em_view.findViewById(R.id.appointment_note);
        Button btnPhoto = (Button) em_view.findViewById(R.id.btn_photo_uploader);
        Button btnAdder =  (Button)em_view.findViewById(R.id.btn_information_adder);
        TextView PhotoTitle  =(TextView) em_view.findViewById(R.id.current_title);
        alertBuilder.setView(em_view);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date mdate = new Date();
                mAppointment.addPath("images/"+mdate.getTime()+".jpg");
                PhotoTitle.setText(mdate.getTime()+".jpg");
            }
        });
        btnAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m_Note =EdNote.getText().toString();
                SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                Date d_instance_date = new Date();
                int i_initdate = 0;
                try{
                    d_instance_date = mdateformat.parse(mAppointment.getDate());
                    i_initdate = (int)(d_instance_date.getTime()/1000);
                }catch (ParseException e){
                    e.printStackTrace();

                }
                final List<String> ImagePaths= mAppointment.getList();
                final String mNote = EdNote.getText().toString();
                mFireBaseInfo.mFirestore.collection("AAppointment")//.whereEqualTo("patientid",mAppointment.getPatientID())
                        //.whereEqualTo("type",mAppointment.getAppointmentType()).whereEqualTo("date",i_initdate)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {//////
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {////
                                if(task.isSuccessful()){

                                    QuerySnapshot query_res = task.getResult();
                                    List<DocumentSnapshot> documents = query_res.getDocuments();

                                    DocumentSnapshot delDocument = documents.get(0);


                                    if(delDocument!=null){
                                        DocumentReference delDocumentRef = delDocument.getReference();
                                        delDocumentRef.update("path",ImagePaths);
                                        delDocumentRef.update("note",mNote);


                                    }
                                    else{

                                    }
                                }
                                else{

                                }

                            }////


                        });//////

                mFurtherUpdateDialog.dismiss();
            }
        });


        mFurtherUpdateDialog = alertBuilder.create();
        mFurtherUpdateDialog.show();

    }
    public void startUpdaterActivity(View v,Appointment instance){
        Intent FurtherUpdate = new Intent(v.getContext(),AppointmentUpdater.class);
        FurtherUpdate.putExtra("apttype",instance.getAppointmentType());
        FurtherUpdate.putExtra("apttime",instance.getDate());
        v.getContext().startActivity(FurtherUpdate);


    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Appointment mAppointment=mAppointmentList.get(position);
        //note from res id to actual bitmap
        //holder.fruitImage.setImageResource(mphoto.getImageID());
        String displayed = position+"  "+mAppointment.getAppointmentType();
        holder.appointmentTag.setText(displayed);
        String appstatus = mAppointment.getStatus();

        switch(appstatus){
            case "Finished":{

                holder.appointmentCardView.setCardBackgroundColor(ContextCompat.getColor( holder.appointmentCardView.getContext(), R.color.lightviolet));
                break;
            }
            case "Scheduled":{
                holder.appointmentCardView.setCardBackgroundColor(ContextCompat.getColor( holder.appointmentCardView.getContext(), R.color.lightgreen));
                break;
            }
            case "Past":{
                holder.appointmentCardView.setCardBackgroundColor(ContextCompat.getColor( holder.appointmentCardView.getContext(), R.color.lightyellow));
                break;
            }
            default:{
                break;
            }
        }
        holder.appointmentChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> SelectedAppointment =  showAppointment(mAppointment);
                final String[] items =  SelectedAppointment.toArray(new String[SelectedAppointment.size()]);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Check Appointment");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        });
                alertBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mShowAppointmentDialog.dismiss();
                    }
                });
                mShowAppointmentDialog = alertBuilder.create();
                mShowAppointmentDialog.show();
            }
        });
        Date ApptDate = new Date();
        ApptDate.setTime((long)1000*mAppointment.getintDate());
        Date Current = new Date();
        if(Current.getTime()-ApptDate.getTime()>0){
            if(!("Finished".equals(mAppointment.getStatus()))) {


                holder.appointmentUpdater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mAppointment
                        final String[] items = {"Finished", "Canceled", "Postponed"};

                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                        alertBuilder.setTitle("Select your appointment feedback.");
                        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mListStack.add(0, items[i]);


                                //Toast.makeText(MainActivity.this, items[i], Toast.LENGTH_SHORT).show();
                            }
                        });


                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //
                                final View vv = v;
                                SimpleDateFormat mdateformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                                Date d_instance_date = new Date();
                                int i_initdate = 0;
                                try {
                                    d_instance_date = mdateformat.parse(mAppointment.getDate());
                                    i_initdate = (int) (d_instance_date.getTime() / 1000);
                                } catch (ParseException e) {
                                    e.printStackTrace();

                                }
                                mListStack.add("Finished");
                                String selectedUpdate = mListStack.get(0);
                                mFireBaseInfo.mFirestore.collection("AAppointment")//.whereEqualTo("date",i_initdate)
                                        .whereEqualTo("patientid", mAppointment.getPatientID()).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    QuerySnapshot query_res = task.getResult();
                                                    List<DocumentSnapshot> documents = query_res.getDocuments();

                                                    DocumentSnapshot delDocument = documents.get(0);
                                                    if (delDocument != null) {
                                                        DocumentReference delDocumentRef = delDocument.getReference();
                                                        delDocumentRef.update("status", selectedUpdate);

                                                    } else {

                                                    }


                                                } else {
                                                    // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                // furtherUpdate(vv,mAppointment);
                                startUpdaterActivity(vv, mAppointment);
                                mUpdateAppointmentDialog.dismiss();
                            }
                        });


                        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mUpdateAppointmentDialog.dismiss();
                            }
                        });
                        mUpdateAppointmentDialog = alertBuilder.create();

                        try {
                            SimpleDateFormat mdateformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                            Date date_time = mdateformat.parse(mAppointment.getDate());
                            ;
                            final Date currentDate = new Date();

                            if (currentDate.getTime() - date_time.getTime() > 0) {
                                mUpdateAppointmentDialog.show();
                            } else {
                                // nothing happened
                            }
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }else{
                holder.appointmentUpdater.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.appointmentUpdater.setVisibility(View.INVISIBLE);
        }

    }
    private List<String> mListStack = new ArrayList<String>();
    private ItemClickListener mItemClickListener;
    public interface ItemClickListener{
        public void onItemClick(int position);
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }




    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView appointmentTag;
       // Button appointmentRemover;
        CardView appointmentCardView;
        Button appointmentChecker;
        Button appointmentUpdater;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);//btnAppointmentUpdater
            appointmentCardView = itemView.findViewById(R.id.appointment_card_view);
            appointmentTag =itemView.findViewById(R.id.appointmnet_text_unit);
            //appointmentRemover =itemView.findViewById(R.id.btnAppointmentRemover);
            appointmentChecker =itemView.findViewById(R.id.btnAppointmentChecker);
            appointmentUpdater =itemView.findViewById(R.id.btnAppointmentUpdater);
        }
    }
    //

    public void showAppointmentDialogue(){





    }
}
