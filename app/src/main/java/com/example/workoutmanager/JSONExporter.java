package com.example.workoutmanager;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class JSONExporter {

    public static File exportWorkoutsToJSON(Context context, ArrayList<Workout> workouts) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (Workout workout : workouts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", workout.getId());
                jsonObject.put("name", workout.getName());
                jsonObject.put("duration", workout.getDuration());
                jsonArray.put(jsonObject);
            }

            File file = new File(context.getExternalFilesDir(null), "workouts.json");
            FileWriter writer = new FileWriter(file);
            writer.write(jsonArray.toString());
            writer.close();

            return file; // Возвращаем файл для подтверждения или дальнейшей обработки

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Возвращаем null, если произошла ошибка
    }
}