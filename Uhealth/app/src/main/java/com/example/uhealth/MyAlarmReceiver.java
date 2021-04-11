package com.example.uhealth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class MyAlarmReceiver extends BroadcastReceiver {
    AlertDialog mAlarmAppointmentDialog;
    private NotificationManager m_notificationMgr = null;
    private static final int NOTIFICATION_FLAG = 3;
    @Override
    public void onReceive(Context context, Intent intent) {
        //String AppointmentType = intent.getStringExtra("type");
        //
        m_notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //
        Intent starter = intent;
        Bundle extras = starter.getExtras();
        if (extras != null) {
            boolean isAppointment = extras.getBoolean("isAppointment", false);
            boolean isMedication= extras.getBoolean("isMedication", false);
            if (isAppointment) {
                Intent ListIntent = new Intent(context, AppointmnetList.class);
                ListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ListIntent.putExtra("isAppointment",true);
                ListIntent.putExtra("typefromalarm","AppointmentReminder");
                ListIntent.putExtra("apttype",extras.getString("apttype"));
                ListIntent.putExtra("apttime",extras.getString("apttime"));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ListIntent, 0);
                Notification notify = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_baseline_favorite_24)
                        .setTicker(extras.getString("apttype"))
                        .setContentTitle("Appointment Reminder!")
                        .setContentText(extras.getString("apttime"))
                        .setContentIntent(pendingIntent)
                        .setNumber(1)
                        .build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                m_notificationMgr .notify(NOTIFICATION_FLAG, notify);


                /*-----------------------------------------
                Intent ListIntent = new Intent(context, AppointmnetList.class);
                ListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ListIntent.putExtra("isAppointment",true);
                ListIntent.putExtra("typefromalarm","AppointmentReminder");
                ListIntent.putExtra("apttype",extras.getString("apttype"));
                ListIntent.putExtra("apttime",extras.getString("apttime"));
                context.startActivity(ListIntent);
                */

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
