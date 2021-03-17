package com.example.uhealth;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uhealth.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Appointment mAppointment=mAppointmentList.get(position);
        //note from res id to actual bitmap
        //holder.fruitImage.setImageResource(mphoto.getImageID());
        String displayed = position+"  "+mAppointment.getAppointmentType();
        holder.appointmentTag.setText(displayed);

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
        /*
        holder.appointmentRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAppointment
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want to remove the appointment?");
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mRemoveAppointmentDialog.dismiss();
                    }
                });
                mRemoveAppointmentDialog = alertBuilder.create();
                mRemoveAppointmentDialog.show();

            }
        });
        */

        holder.appointmentUpdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAppointment
                final String[] items = {"Finished","Canceled","Postponed"};

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Select your appointment feedback.");
                alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mListStack.add(0,items[i]);


                        //Toast.makeText(MainActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                });


                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selectedUpdate  = mListStack.get(0);
                        mFireBaseInfo.mFirestore.collection("Appointment").whereEqualTo("date",mAppointment.getDate())
                                .whereEqualTo("patientid",mAppointment.getPatientID()).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            QuerySnapshot query_res =  task.getResult();
                                            List<DocumentSnapshot> documents = query_res.getDocuments();

                                            DocumentSnapshot delDocument = documents.get(0);
                                            if(delDocument!=null ){
                                                DocumentReference delDocumentRef = delDocument.getReference();
                                                delDocumentRef.update("stauts",selectedUpdate );

                                            }else{

                                            }


                                        } else {
                                            // Toast.makeText(ProfileActivity.this, "Ouch!!!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
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

                try{
                    SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                    Date date_time = mdateformat.parse(mAppointment.getDate());;
                    final Date currentDate = new Date();

                    if(currentDate.getTime() - date_time.getTime() > 0){
                        mUpdateAppointmentDialog.show();
                    }else{
                        // nothing happened
                    }
                }catch(java.text.ParseException e){
                    e.printStackTrace();
                }


            }
        });
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
        Button appointmentChecker;
        Button appointmentUpdater;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);//btnAppointmentUpdater
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
