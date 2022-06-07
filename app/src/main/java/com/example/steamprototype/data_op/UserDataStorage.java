package com.example.steamprototype.data_op;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.steamprototype.entity.User;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataStorage {
    public static final String SP_NAME = "UserData";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Connection connection;
    private String dbname = "steam_prototype";

    public UserDataStorage(Context context) {

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

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean saveUserData(User user) {
        try {
            Statement statement = this.connection.createStatement();
            String query = String.format("select username from user where username = %s;", user.getUsername());
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                PreparedStatement pst = this.connection.prepareStatement("insert into user values(?,?,?);");
                pst.setString(1, user.getUsername());
                pst.setString(2, user.getPassword());
                pst.setString(3, user.getEmail());
                pst.executeUpdate();
            } else {
                return false;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public User getUserData(String name, String pass) {
        User user = null;
        try {
            Statement statement = this.connection.createStatement();
            String query = String.format("select * from user where username = %s && password = %s;", name, pass);
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                user = new User(name, pass, result.getString(3));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void saveCurrentUser() {

    }

    public void getCurrentUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
