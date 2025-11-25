package com.gegosoft.yourappname.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;

import com.gegosoft.yourappname.Receiver.NotificationReceiver;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Activity.MainActivity;
import com.gegosoft.yourappname.Models.NotificationPoJo;
import com.gegosoft.yourappname.Models.UpdateFCMTokenModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.NotificationDataBase;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    Api apiInterface;
    NotificationDataBase notificationDataBase;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> map = remoteMessage.getData();
        String myCustomKey = map.get("type");
        CreateNotificationMessage(this,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage);
    }
    private void CreateNotificationMessage(Context ctx, String title, String Msg, RemoteMessage remoteMessage){

        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);

            title =  object.get("message").toString();
            Msg=  object.get("type").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        notificationDataBase = new NotificationDataBase(this);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);

        if (Msg.equalsIgnoreCase("notice")){
            userDetailsSharedPref.saveBoolean("isNotice",true);
            ShowNotication(remoteMessage,title,Msg);

        }
        else if(Msg.equalsIgnoreCase("assignment")){
            userDetailsSharedPref.saveBoolean("isAssignment",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("video_room")){
            userDetailsSharedPref.saveBoolean("isVideoroom",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("discipline")){
            userDetailsSharedPref.saveBoolean("isDiscipline",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("private message")){
            userDetailsSharedPref.saveBoolean("isPrivateMessage",true);
            NotificationPoJo notificationPoJo =new NotificationPoJo();
            notificationPoJo.setPrivateMessage(true);
            notificationDataBase.addMessageNotification(notificationPoJo);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("homework")){
            userDetailsSharedPref.saveBoolean("isHomeWork",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("birthday")){
            userDetailsSharedPref.saveBoolean("isBirthday",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("performance")){
            userDetailsSharedPref.saveBoolean("isPerformance",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("leave")){
            userDetailsSharedPref.saveBoolean("isLeave",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("bulletin")){
            userDetailsSharedPref.saveBoolean("isMagazine",true);
            ShowNotication(remoteMessage,title,Msg);
        }
        else if(Msg.equalsIgnoreCase("conversation")){
            userDetailsSharedPref.saveBoolean("isFeedback",true);
            ShowNotication(remoteMessage,title,Msg);
        }
    }

    private void ShowNotication(RemoteMessage remoteMessage, String title, String Msg){
        int id=15;
        Intent intent= new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder b = new Notification.Builder(this);

        NotificationChannel mChannel = null;
        b.setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(Msg)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("cid", "name", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setShowBadge(true);
            b.setChannelId("cid");
            mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build());
        }

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }
        b.setAutoCancel(true);
        b.setOngoing(false);
        notificationManager.notify(id, b.build());
        sendBroadcast(new Intent(NotificationReceiver.ACTION_NOTIFICATION).putExtra("MessageReceived",remoteMessage));
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        userDetailsSharedPref.saveString("firebasetoken",token);
        if (userDetailsSharedPref.getString("token")!=null){
            sendRegistrationToServer(token);
        }

    }
    private void sendRegistrationToServer(final String refreshedToken){
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        apiInterface  = ApiClient.getClient().create(Api.class);
        Call<UpdateFCMTokenModel> tokenModelCall = apiInterface.updatefcmtoken(headermap,refreshedToken);
        tokenModelCall.enqueue(new Callback<UpdateFCMTokenModel>() {
            @Override
            public void onResponse(Call<UpdateFCMTokenModel> call, Response<UpdateFCMTokenModel> response) {
                if (response.isSuccessful()){

                }
            }
            @Override
            public void onFailure(Call<UpdateFCMTokenModel> call, Throwable t) {

            }
        });
    }

}
