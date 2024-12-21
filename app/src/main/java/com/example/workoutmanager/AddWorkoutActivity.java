package com.example.workoutmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddWorkoutActivity extends AppCompatActivity {

    private WorkoutDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        dbHelper = new WorkoutDatabaseHelper(this);

        EditText workoutNameInput = findViewById(R.id.workout_name_input);
        EditText workoutDurationInput = findViewById(R.id.workout_duration_input);
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> {
            String name = workoutNameInput.getText().toString().trim();
            String durationText = workoutDurationInput.getText().toString().trim();

            if (name.isEmpty() || durationText.isEmpty()) {
                // Показываем сообщение об ошибке
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return; // Выходим, если поля пустые
            }

            try {
                int duration = Integer.parseInt(durationText);
                dbHelper.addWorkout(name, duration);
                Toast.makeText(this, "Тренировка добавлена!", Toast.LENGTH_SHORT).show();
                finish(); // Закрываем Activity
            } catch (NumberFormatException e) {
                // Обрабатываем случай, если пользователь ввел некорректное число
                Toast.makeText(this, "Введите правильную длительность (число)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}