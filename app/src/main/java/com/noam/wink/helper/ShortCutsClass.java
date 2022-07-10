package com.noam.wink.helper;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.noam.wink.helper.uihelper.ReminderBroadcast;
import com.noam.wink.model.Time;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ShortCutsClass {

    public static void toastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String getHourString(long time) {

        if (time > 3600000) {
            return String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(time),
                    TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        }

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }


    public static String getTimerText(long overTime) {

        int rounded = (int) Math.round(overTime);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        if (hours > 0) {
            return String.format("%02d:%02d:%2d", hours, minutes, seconds);
        }

        return String.format("%02d:%02d", minutes, seconds);

    }


    public static Time getTimeSeparate(int timeInt) {
        Time time = new Time();
        time.setMinute(timeInt % 60);
        time.setHour(timeInt / 60);
        return time;
    }


    public static String getTimeFormatted(Time time) {
        if (time.getHour() > 0) {
            String s = String.format("%02d:%02d", time.getHour(), time.getMinute());
            return s + " שעות";
        } else return time.getMinute() + " דקות";
    }


    private void setAlarmTime(Context context, long time, String text, String name, int image) {

        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra("text", text);
        intent.putExtra("text", name);
        intent.putExtra("image", image);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }


    private void cancelAlarmTime(Context context, long time) {

        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Wink";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Wink", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);


        }
    }

}
