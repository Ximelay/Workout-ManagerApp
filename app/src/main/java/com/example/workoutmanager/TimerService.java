package com.example.workoutmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class TimerService extends Service {

    private static final String CHANNEL_ID = "timer_channel";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String workoutName = intent.getStringExtra("workout_name");
        int duration = intent.getIntExtra("workout_duration", 0); // Длительность в миллисекундах

        createNotificationChannel();

        // Создание уведомления для Foreground Service
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Тренировка")
                .setContentText("Тренировка: " + workoutName + " началась")
                .setSmallIcon(android.R.drawable.ic_dialog_info);

        startForeground(1, notification.build());

        // Таймер обратного отсчета
        if (duration > 0) {
            new CountDownTimer(duration, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Логирование оставшегося времени (опционально)
                    // Log.d("TimerService", "Осталось времени: " + millisUntilFinished / 1000 + " секунд");
                }

                @Override
                public void onFinish() {
                    notifyUser(workoutName + " завершена!");
                    stopSelf();
                }
            }.start();
        }

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Таймер тренировки",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void notifyUser(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Тренировка завершена")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify(2, builder.build());
        }
    }
}