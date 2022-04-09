package com.example.safemedicare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    String eventName, eventDescription;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            eventName = extras.getString("EVENT_NAME");
            eventDescription = extras.getString("EVENT_DESCRIPTION");
            //Toast.makeText(getApplicationContext(), "Welcome "+name, Toast.LENGTH_SHORT).show();
        }
        NotificationClass notificationClass = new NotificationClass(context);
        NotificationCompat.Builder notification = notificationClass.getChannelNotification(eventName, eventDescription);
        notificationClass.getManager().notify(1, notification.build());


    }
}
