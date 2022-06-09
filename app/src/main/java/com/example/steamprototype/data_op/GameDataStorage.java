package com.example.steamprototype.data_op;

import android.database.sqlite.SQLiteDatabase;

import com.example.steamprototype.MainActivity;

public class GameDataStorage {
    private SQLiteDatabase database;

    public GameDataStorage() {
        this.database = MainActivity.userDataStorage.getDatabase();
        this.createTable();
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS game ( gameID INTEGER PRIMARY KEY," +
                "title VARCHAR(60)," +
                "publisher VARCHAR(30)," +
                "developer VARCHAR(30)," +
                "genre VARCHAR(40)," +
                "platform VARCHAR(40)," +
                "releaseDate DATETIME," +
                "price DOUBLE," +
                "discount DOUBLE," +
                "image TEXT )";
        database.execSQL(query);
    }


}
