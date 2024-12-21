package com.example.workoutmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private WorkoutDatabaseHelper dbHelper;
    private ArrayList<Workout> workouts; // Объявление переменной
    private ArrayList<String> workoutList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new WorkoutDatabaseHelper(this);
        workouts = new ArrayList<>();
        workoutList = new ArrayList<>();

        ListView listView = findViewById(R.id.workout_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workoutList);
        listView.setAdapter(adapter);

        loadWorkouts();

        // Обработчик для кнопки добавления тренировки
        findViewById(R.id.add_workout_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddWorkoutActivity.class);
            startActivity(intent);
        });

        // Обработчик для экспорта в JSON
        Button exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(v -> {
            File jsonFile = JSONExporter.exportWorkoutsToJSON(MainActivity.this, workouts);
            if (jsonFile != null) {
                Toast.makeText(this, "Экспорт выполнен: " + jsonFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ошибка при экспорте!", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик для выбора тренировки
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Workout selectedWorkout = workouts.get(position); // Используем объект Workout
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            intent.putExtra("workout_id", selectedWorkout.getId());
            intent.putExtra("workout_name", selectedWorkout.getName());
            intent.putExtra("workout_duration", selectedWorkout.getDuration());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWorkouts(); // Загрузка данных из базы при возвращении на экран
    }

    private void loadWorkouts() {
        workoutList.clear();
        workouts.clear(); // Очищаем список объектов Workout

        // Получаем тренировки из базы данных
        workouts = dbHelper.getAllWorkouts();

        for (Workout workout : workouts) {
            workoutList.add(workout.getName()); // Добавляем имя тренировки в ListView
        }

        adapter.notifyDataSetChanged();
    }
}