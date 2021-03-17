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
      // Intent ListIntent = new Intent(context, AppointmnetList.class);
      // ListIntent.putExtra("typefromalarm","AppointmentReminder");
      //// Toast.makeText(context, "Don't forget your appointment tomorrow!", Toast.LENGTH_LONG).show();
       // context.startActivity(ListIntent);
        Intent starter = intent;
        Bundle extras = starter.getExtras();
        if (extras != null) {
            boolean isAppointment = extras.getBoolean("isAppointment", false);
            boolean isMedication= extras.getBoolean("isMedication", false);
            if (isAppointment) {
                //-----------------------------------------
                //Toast.makeText(context, "Don't forget your" + extras.getString("apttype")+"at"+extras.getString("apttime"), Toast.LENGTH_LONG).show();


            } else {

                //Do nothing
            }

            if(isMedication){
                Toast.makeText(context, "Don't forget to take your" + extras.getString("medicine")+"at"+extras.getString("medtime"), Toast.LENGTH_LONG).show();

            }else{

            }
        }

    }

}
