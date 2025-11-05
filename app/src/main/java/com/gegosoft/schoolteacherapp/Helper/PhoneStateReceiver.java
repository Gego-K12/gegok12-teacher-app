package com.gegosoft.schoolteacherapp.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.widget.Toast;

public class PhoneStateReceiver extends BroadcastReceiver {
    Context context;
    public static String ACTION_PHONE = "android.intent.action.PHONE_STATE";
    @Override
    public void onReceive(Context context, Intent intent) {


    }
    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == 1) {
                String msg = "New Phone Call Event. Incomming Number : "+incomingNumber;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, msg, duration);
                toast.show();

            }
        }
    }

}
