package com.example.uhealth;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhealth.Activity.MainActivity;

import java.util.ArrayList;
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
        String displayed = position+"";
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
        holder.appointmentUpdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAppointment
                final String[] items = {"office appointment","chemotherapy","surgery or procedure appointment","radiation session","imaging appointment"};

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Select your appointment type.");
                alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Toast.makeText(MainActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                });


                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //send result to firebase

                    }
                });


                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mUpdateAppointmentDialog.dismiss();
                    }
                });
                mUpdateAppointmentDialog = alertBuilder.create();
                mUpdateAppointmentDialog.show();

            }
        });
    }
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
        Button appointmentRemover;
        Button appointmentChecker;
        Button appointmentUpdater;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);//btnAppointmentUpdater
            appointmentTag =itemView.findViewById(R.id.appointmnet_text_unit);
            appointmentRemover =itemView.findViewById(R.id.btnAppointmentRemover);
            appointmentChecker =itemView.findViewById(R.id.btnAppointmentChecker);
            appointmentUpdater =itemView.findViewById(R.id.btnAppointmentUpdater);
        }
    }
    //

    public void showAppointmentDialogue(){





    }
}
