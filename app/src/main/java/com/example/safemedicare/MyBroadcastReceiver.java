package com.example.safemedicare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();

        NotificationClass notificationClass = new NotificationClass(context);
        NotificationCompat.Builder notification = notificationClass.getChannelNotification();
        notificationClass.getManager().notify(1, notification.build());



    }
}
