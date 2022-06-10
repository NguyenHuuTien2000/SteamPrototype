package com.example.steamprototype.data_op;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLibraryStorage {
    private SQLiteDatabase database = MainActivity.userDataStorage.getDatabase();

    public UserLibraryStorage() {

    }

    public void createTable() {
        database.execSQL("DROP TABLE library");
        String query = "CREATE TABLE IF NOT EXISTS library ( " +
                "gameID INTEGER PRIMARY KEY NOT NULL," +
                "username VARCHAR(20) REFERENCES user(username)," +
                "dateAdded DATETIME NOT NULL )";
        database.execSQL(query);
    }

    public void addGameToLibrary(User user, Game game) {
        user.addToLibrary(game);
        ContentValues contentValues = new ContentValues();
        contentValues.put("gameID", game.getGameID());
        contentValues.put("username", user.getUsername());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        contentValues.put("dateAdded", formatter.format(date));

        this.database.insert("library", null, contentValues);
    }


}
