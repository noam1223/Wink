package com.noam.wink.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noam.wink.model.Task;
import com.noam.wink.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataBaseLocally {

    private static final String TASK_TAG = "save_user";



    public static void saveUserLocally(Context context, User user) {

        SharedPreferences sharedPref = context.getSharedPreferences(TASK_TAG, Context.MODE_PRIVATE);

        SharedPreferences.Editor prefEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefEditor.putString(TASK_TAG, json);
        prefEditor.commit();


    }



    public static void saveListLocally(Context context, List<Task> tasks) {

        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(tasks);

        SharedPreferences sharedPref = context.getSharedPreferences(TASK_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(TASK_TAG, jsonCurProduct);
        editor.commit();

    }



    public static List<Task> getDataFromSharedPreferences(Context context){
        Gson gson = new Gson();
        List<Task> productFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(TASK_TAG, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(TASK_TAG, "");

        Type type = new TypeToken<List<Task>>() {}.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        if (productFromShared != null)
            return productFromShared;
        else return new ArrayList<>();
    }


    public static User getUserFromSharedPreferences(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(TASK_TAG, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String jsonPreferences = sharedPref.getString(TASK_TAG, "");
        return gson.fromJson(jsonPreferences, User.class);
    }

}
