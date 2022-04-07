package com.example.safemedicare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationClass {
    Context context;
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager notificationManager;

    public NotificationClass(Context context) {
        this.context = context;
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

            getManager().createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(context, channelID)
                .setContentTitle("Alarm!")
                .setContentText("Your AlarmManager is working.")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24);
    }
}
