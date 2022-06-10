package com.example.steamprototype.data_op;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserLibraryStorage {
    private SQLiteDatabase database = MainActivity.userDataStorage.getDatabase();
    private List<Game> fullList;

    public UserLibraryStorage(ArrayList<Game> fullList) {
        this.fullList = fullList;
        this.createTable();
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS library ( " +
                "username VARCHAR(20) PRIMARY KEY NOT NULL," +
                "gameID INTEGER REFERENCES game(gameID)," +
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

    public void loadUserLibrary(User user) {
        List<Game> library = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM library WHERE username == '" + user.getUsername() + "'", null);
        while (cursor.moveToNext()) {
            int gameID = Integer.parseInt(cursor.getString(1));
            library.add(fullList.get(gameID));
        }
        user.setLibrary(library);
    }


}
