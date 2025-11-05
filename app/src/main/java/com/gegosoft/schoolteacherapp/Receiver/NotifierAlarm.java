package com.gegosoft.schoolteacherapp.Receiver;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.gegosoft.schoolteacherapp.Activity.To_Do_List_Activity;
import com.gegosoft.schoolteacherapp.Models.Reminders;
import com.gegosoft.schoolteacherapp.R;

import java.text.ParseException;

public class NotifierAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Reminders reminder = new Reminders();
        reminder.setMessage(intent.getStringExtra("Message"));
        try {
            reminder.setRemindDate(java.text.DateFormat.getDateInstance().parse((intent.getStringExtra("RemindDate"))));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        Intent intent1 = new Intent(context, To_Do_List_Activity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(To_Do_List_Activity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        NotificationChannel channel = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel = new NotificationChannel("my_channel_01","hello", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(alarmsound,attributes);

        }

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = builder.setContentTitle("Reminder")
                    .setContentText(intent.getStringExtra("Message"))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(intent2)
                    .setChannelId("my_channel_01")
                    .build();
        } else {
            notification = builder.setContentTitle("Reminder")
                    .setContentText(intent.getStringExtra("Message"))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(intent2)
                    .build();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);

    }
}

