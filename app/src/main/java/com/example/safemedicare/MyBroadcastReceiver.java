package com.example.safemedicare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver  {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder( context,"My Notification");
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_active_24);
        builder.setContentTitle("Event Name: ");
        builder.setContentText("Event Description: ");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);


        // to notify the user
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        // help id give anything / build create notification
        managerCompat.notify(1, builder.build());


    }
}
