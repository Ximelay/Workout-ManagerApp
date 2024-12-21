package com.example.workoutmanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TimerActivity extends AppCompatActivity {

    private String workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Запрос разрешения для уведомлений (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU = API 33
            if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS")
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
            }
        }

        workoutName = getIntent().getStringExtra("workout_name");
        int workoutDuration = getIntent().getIntExtra("workout_duration", 0); // Длительность в минутах

        TextView workoutTitle = findViewById(R.id.workout_title);
        workoutTitle.setText(workoutName);

        Button startTimerButton = findViewById(R.id.start_timer_button);
        startTimerButton.setOnClickListener(v -> {
            if (workoutDuration <= 0) {
                Toast.makeText(this, "Длительность тренировки должна быть больше 0", Toast.LENGTH_SHORT).show();
                return;
            }
            startTimer(workoutName, workoutDuration);
        });
    }

    private void startTimer(String workoutName, int duration) {
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("workout_name", workoutName);
        intent.putExtra("workout_duration", duration * 60 * 1000); // Переводим минуты в миллисекунды
        startService(intent);
    }
}