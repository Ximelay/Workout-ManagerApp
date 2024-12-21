package com.example.workoutmanager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONExporter {

    private static final String TAG = "JSONExporter";

    public static File exportWorkoutsToJSON(Context context, ArrayList<Workout> workouts) {
        try {
            // Преобразуем список тренировок в JSON
            JSONArray jsonArray = new JSONArray();
            for (Workout workout : workouts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", workout.getId());
                jsonObject.put("name", workout.getName());
                jsonObject.put("duration", workout.getDuration());
                jsonArray.put(jsonObject);
            }

            String jsonString = jsonArray.toString(4); // Преобразуем в строку с отступами
            Log.d(TAG, "Экспортированные данные: " + jsonString);

            // Сохраняем JSON в файл
            File jsonFile = saveJsonToFile(context, jsonString);
            return jsonFile;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка при экспорте в JSON", e);
            return null;
        }
    }

    private static File saveJsonToFile(Context context, String jsonString) {
        try {
            // Создаем директорию "exports" внутри директории приложения
            File dir = new File(context.getExternalFilesDir(null), "exports");
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(TAG, "Ошибка создания директории: " + dir.getAbsolutePath());
                return null;
            }

            // Создаем файл workouts.json
            File jsonFile = new File(dir, "workouts.json");
            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write(jsonString);
            }

            Log.d(TAG, "JSON сохранен в файл: " + jsonFile.getAbsolutePath());
            return jsonFile;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка при сохранении JSON в файл", e);
            return null;
        }
    }
}