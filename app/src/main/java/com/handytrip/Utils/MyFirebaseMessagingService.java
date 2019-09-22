package com.handytrip.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.handytrip.MainActivity;
import com.handytrip.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public RetrofitAPI api;
    public boolean isSuccess = false;
    public Preferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        api = RetrofitInit.getRetrofit();
        pref = new Preferences(this);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        pref.setFcmToken(s);
        sendFcmToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("remoteMessage", remoteMessage.getData().get("message"));
//        Toast.makeText(this, remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        if (pref.isGetNotification()) {
            sendNotification(remoteMessage.getData().get("message"));
        }
    }

    public boolean sendFcmToken(String token) {
        Log.d("sendedToken-Fire", token);
        Call<String> sendFcmToken = api.sendFcmToken(pref.getUserId(), token);
        sendFcmToken.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isSuccess = true;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return isSuccess;
    }


    private void sendNotification(String messageBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "1";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.app_icon_round)
                .setContentTitle("새 알림")
                .setContentText(messageBody);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
