package com.example.steamprototype.data_op;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.steamprototype.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class UserDataStorage {
    private SQLiteDatabase database;
    public static final String SP_NAME = "UserData";
    public boolean firstRun = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public UserDataStorage(Context context, Activity activity) {
        innitDB(activity);
        this.sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);

        this.editor = sharedPreferences.edit();
        this.editor.putBoolean("firstRun", this.firstRun);
        this.editor.commit();
    }

    public boolean checkFirstRun() {
        return this.sharedPreferences.getBoolean("firstRun", true);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void innitDB(Activity activity) {
        String dbname = "steam_prototype.sqlite";
        try {
            String savePath = activity.getApplicationInfo().dataDir + "/databases/";
            File file = new File(savePath + dbname);
            if (!file.exists()) {
                this.firstRun = true;
                //this.clearCurrentUser();

                InputStream inputStream = activity.getAssets().open(dbname);
                File folder = new File(savePath);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(savePath + dbname);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.database = activity.openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
    }

    public boolean saveUserData(User user) {
        Cursor cursor = this.database.rawQuery("SELECT * FROM user", null);
        //cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            String username = cursor.getString(0);
            String password = cursor.getString(1);
            String email = cursor.getString(2);
            if (username.equals(user.getUsername()) || password.equals(user.getPassword()) || email.equals(user.getEmail())) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("email", user.getEmail());
        this.database.insert("user", null, contentValues);
        cursor.close();
        return true;
    }

    public User getUserData(String name, String pass) {
        Cursor cursor = this.database.rawQuery("SELECT * FROM user", null);
        while (cursor.moveToNext()) {
            String username = cursor.getString(0);
            String password = cursor.getString(1);
            if (username.equals(name) && password.equals(pass)) {
                return new User(username, password, cursor.getString(2));
            }
        }
        cursor.close();
        return null;
    }

    public boolean checkContains() {
        Cursor cursor = this.database.rawQuery("SELECT * FROM user", null);
        boolean res = cursor.moveToFirst();
        cursor.close();
        return res;
    }

    public void saveCurrentUser(String name, String pass, String email) {
        this.editor = sharedPreferences.edit();
        this.editor.putString("name", name);
        this.editor.putString("pass", pass);
        this.editor.putString("email", email);
        this.editor.commit();
    }

    public User getCurrentUser() {
        String username = sharedPreferences.getString("name","");
        String password = sharedPreferences.getString("pass", "");
        String email = sharedPreferences.getString("email", "");
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return null;
        }
        return new User(username, password, email);
    }

    public void clearCurrentUser() {
        this.editor = sharedPreferences.edit();
        this.editor.clear();
        this.editor.commit();
    }
}
