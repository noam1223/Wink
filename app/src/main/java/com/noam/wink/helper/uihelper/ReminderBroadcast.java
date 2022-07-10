package com.noam.wink.helper.uihelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.noam.wink.R;

public class ReminderBroadcast extends BroadcastReceiver {


    long twoHoursInMilliSeconds = 600000; // ten minutes
    long halfHour = 300000;// five minutes
    int firstCode = 200, eatCode = 201, drinkCode = 202;
//    boolean firstTime = true;

    @Override
    public void onReceive(final Context context, Intent intent) {

        final NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        final NotificationCompat.Builder builder;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_collapsed);

        String text = (String) intent.getStringExtra("text");
        String name = (String) intent.getStringExtra("name");
        int image = (int) intent.getIntExtra("image", -1);


        builder = new NotificationCompat.Builder(context, "Wink")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContent(remoteViews);

        remoteViews.setTextViewText(R.id.name_text_view_notification_collapsed,"היי " + name);
        remoteViews.setTextViewText(R.id.msg_text_view_notification_collapsed,text);

        if (image > -1)
            remoteViews.setImageViewResource(R.id.img_view_icon_notification_collapsed, image);

        notificationCompat.notify(firstCode, builder.build());


    }
}
