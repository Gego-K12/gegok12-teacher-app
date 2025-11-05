package com.gegosoft.schoolteacherapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationReceiver extends BroadcastReceiver {
    UserDetailsSharedPref userDetailsSharedPref;

    public static String ACTION_NOTIFICATION = "com.gegosoft.schoolteacherapp.NotificationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        userDetailsSharedPref = userDetailsSharedPref.getInstance(context);
        RemoteMessage message = intent.getParcelableExtra("MessageReceived");
        if (message != null) {
            JSONObject jsonObject = new JSONObject(message.getData());
            String type = null;
            try {
                type = jsonObject.getString("type");
                if (type.equalsIgnoreCase("event")) {
                    userDetailsSharedPref.saveBoolean("isEventNotification", true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
