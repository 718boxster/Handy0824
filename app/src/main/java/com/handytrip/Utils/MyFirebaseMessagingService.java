package com.handytrip.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.navi_title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "1",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("HandyTrip");
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            Notification notification = new Notification.Builder(this)
                    .setContentText(messageBody)
                    .setSmallIcon(R.drawable.navi_title)
                    .setChannelId(channelId)
                    .build();
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, notification);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
