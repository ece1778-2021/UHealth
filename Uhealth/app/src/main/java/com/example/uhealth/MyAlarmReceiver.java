package com.example.uhealth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class MyAlarmReceiver extends BroadcastReceiver {
    private static final int APPOINTMENT_CHANNEL = 5;
    private static final int MEDICATION_CHANNEL = 6;
    AlertDialog mAlarmAppointmentDialog;
    private NotificationManager m_notificationMgr = null;
    private static final int NOTIFICATION_FLAG = 3;
    private void createNotificationChannel(Context context,NotificationManager notificationManager) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Chanel";
            String description = "default description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notification channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
             //notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //String AppointmentType = intent.getStringExtra("type");
        //
        m_notificationMgr = (NotificationManager) context.getSystemService(NotificationManager.class);
        createNotificationChannel(context,m_notificationMgr);
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
                Notification notify = new Notification.Builder(context,"notification channel")
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
                /////////////////////
                PendingIntent mpendingIntent = PendingIntent.getActivity(context, 0, MedIntent, 0);
                Notification notify = new Notification.Builder(context,"notification channel")
                        .setSmallIcon(R.drawable.ic_baseline_favorite_24)
                        .setTicker(extras.getString("medicine"))
                        .setContentTitle("Medication Reminder!")
                        .setContentText(extras.getString("medtime"))
                        .setContentIntent(mpendingIntent)
                        .setNumber(1)
                        .build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                m_notificationMgr .notify(NOTIFICATION_FLAG, notify);
                ///////////////////////
                //context.startActivity(MedIntent);

            }else{

            }
        }

    }

}
