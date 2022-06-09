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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataStorage {
    private SQLiteDatabase database;
    public static final String SP_NAME = "UserData";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String dbname = "steam_prototype.sqlite";

    public UserDataStorage(Context context, Activity activity) {
        innitDB(activity);
        this.sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }



    public void innitDB(Activity activity) {
        try {
            String savePath = activity.getApplicationInfo().dataDir + "/databases/";
            File file = new File(savePath + this.dbname);
            if (!file.exists()) {
                InputStream inputStream = activity.getAssets().open(this.dbname);
                File folder = new File(savePath);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(savePath + this.dbname);
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
        this.database = activity.openOrCreateDatabase(this.dbname, Context.MODE_PRIVATE, null);
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
        return null;
    }

    public boolean checkContains() {
        Cursor cursor = this.database.rawQuery("SELECT * FROM user", null);
        boolean res = cursor.moveToFirst();
        cursor.close();
        return res;
    }

    public void saveCurrentUser() {

    }

    public void getCurrentUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
