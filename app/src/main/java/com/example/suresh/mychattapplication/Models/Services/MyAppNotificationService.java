package com.example.suresh.mychattapplication.Models.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.example.suresh.mychattapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyAppNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //getting notification data from the message payload
        String notificationTitle=remoteMessage.getNotification().getTitle();
        String notificationBody=remoteMessage.getNotification().getBody();
        String notificationAction=remoteMessage.getNotification().getClickAction();

        //getting extra data from the message payload
        Intent targetIntent=new Intent(notificationAction);
        targetIntent.putExtra("fullname",remoteMessage.getData().get("fullname"));
        targetIntent.putExtra("targetUserID",remoteMessage.getData().get("targetUserID"));
        targetIntent.putExtra("loggedInUserID",remoteMessage.getData().get("loggedInUserID"));
        targetIntent.putExtra("address",remoteMessage.getData().get("address"));
        targetIntent.putExtra("imageURI",remoteMessage.getData().get("imageURI"));

        PendingIntent pendingIntent=PendingIntent.getActivity(
                this,0,
                targetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //setting up parameters for notification to be shown
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this,"chanel1")
                                    .setSmallIcon(R.mipmap.ic_launcher_background)
                                    .setContentTitle(notificationTitle)
                                    .setContentText(notificationBody)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setContentIntent(pendingIntent);

        //invoking notification setup above
        NotificationManager notificationManager= (NotificationManager) getSystemService(
                                                            Context.NOTIFICATION_SERVICE);

        if(notificationManager!=null) {
            notificationManager.notify((int) System.currentTimeMillis(), notification.build());
            }
    }
}
