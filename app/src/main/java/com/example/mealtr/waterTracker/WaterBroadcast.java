package com.example.mealtr.waterTracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mealtr.MainActivity;
import com.example.mealtr.R;

public class WaterBroadcast extends BroadcastReceiver {
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "WATER_NOTIFICATION");
        builder.setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Drink Water!")
                .setContentText("Remember to rehydrate!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent r_intent = new Intent(context, MainActivity.class);
        r_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, r_intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(0, builder.build());
    }
}
