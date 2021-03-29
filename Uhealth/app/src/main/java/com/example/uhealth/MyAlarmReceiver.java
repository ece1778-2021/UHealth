package com.example.uhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class MyAlarmReceiver extends BroadcastReceiver {
    AlertDialog mAlarmAppointmentDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        //String AppointmentType = intent.getStringExtra("type");

        Intent starter = intent;
        Bundle extras = starter.getExtras();
        if (extras != null) {
            boolean isAppointment = extras.getBoolean("isAppointment", false);
            boolean isMedication= extras.getBoolean("isMedication", false);
            if (isAppointment) {
                //-----------------------------------------
                Intent ListIntent = new Intent(context, AppointmnetList.class);
                ListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ListIntent.putExtra("isAppointment",true);
                ListIntent.putExtra("typefromalarm","AppointmentReminder");
                ListIntent.putExtra("apttype",extras.getString("apttype"));
                ListIntent.putExtra("apttime",extras.getString("apttime"));
                context.startActivity(ListIntent);


            } else {

                //Do nothing
            }

            if(isMedication){
                Intent MedIntent = new Intent(context, MedicationList.class);
                MedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MedIntent.putExtra("isMedication",true);
                MedIntent.putExtra("medicine",starter.getStringExtra("medicine"));//medicine
                context.startActivity(MedIntent);

            }else{

            }
        }

    }

}
