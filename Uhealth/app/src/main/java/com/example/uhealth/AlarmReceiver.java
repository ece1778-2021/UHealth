package com.example.uhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String AppointmentType = intent.getStringExtra("type");
        Intent ListIntent = new Intent(context, AppointmnetList.class);
        ListIntent.putExtra("typefromalarm",AppointmentType);
        context.startActivity(ListIntent);
       // Toast.makeText(context, "Don't forget your appointment tomorrow!", Toast.LENGTH_LONG).show();
    }
}
