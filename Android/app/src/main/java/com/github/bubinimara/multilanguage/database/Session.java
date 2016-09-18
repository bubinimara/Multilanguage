package com.github.bubinimara.multilanguage.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by davide on 14/09/16.
 */
public class Session {
    private static final String PREF_FILE = "session";
    private static final String KEY_INIT = "init";
    private static final String KEY_DEFAULT_LANGUAGE = "language";


    public static void setInitialized(@NonNull Context context,boolean isInitialized) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(KEY_INIT,isInitialized).apply();
    }

    public static boolean isInitialized(@NonNull Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_INIT,false);
    }

    public static void setDefaultLanguage(Context context, int languageId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(KEY_DEFAULT_LANGUAGE,languageId).apply();
    }
    public static int getDefaultLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_DEFAULT_LANGUAGE,-1);
    }

    public static void clearAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
