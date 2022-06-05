package com.example.steamprototype.data_op;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.steamprototype.entity.User;

public class UserDataStorage {
    public static final String SP_NAME = "UserData";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final String USERNAME_KEY = "user", PASSWORD_KEY = "pass", EMAIL_KEY = "email";

    public UserDataStorage(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }

    public void saveUserData(User user) {
        editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, user.getUsername());
        editor.putString(PASSWORD_KEY, user.getPassword());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.commit();
    }

    public User getUserData() {
        String username = sharedPreferences.getString(USERNAME_KEY,"");
        String password = sharedPreferences.getString(PASSWORD_KEY, "");
        String email = sharedPreferences.getString(EMAIL_KEY, "");
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return null;
        }
        return new User(username, password, email);
    }

    public void clearUserData() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
