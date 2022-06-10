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
    private List<Game> wishlist;


    public UserLibraryStorage(ArrayList<Game> fullList, ArrayList<Game> wishlist) {
        this.fullList = fullList;
        this.wishlist = wishlist;
        this.createTable();
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS library ( " +
                "username VARCHAR(20) REFERENCES user(username)," +
                "gameID INTEGER REFERENCES game(gameID)," +
                "dateAdded DATETIME NOT NULL )";
        database.execSQL(query);
    }

    public boolean checkContain(User user, Game game) {
        List<Game> library = user.getLibrary();
        for (Game libGame : library) {
            if (game.getGameID() == libGame.getGameID()) {
                return true;
            }
        }
        return false;
    }

    public boolean addGameToLibrary(User user, Game game) {
        if (checkContain(user, game)) {
            return false;
        }
        user.addToLibrary(game);
        ContentValues contentValues = new ContentValues();
        contentValues.put("gameID", game.getGameID());
        contentValues.put("username", user.getUsername());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        contentValues.put("dateAdded", formatter.format(date));

        this.database.insert("library", null, contentValues);
        return true;
    }

    public void addGameToWL(User user, Game game) {
        user.addToWishlist(game);
        ContentValues contentValues = new ContentValues();
        contentValues.put("gameID", game.getGameID());
        contentValues.put("username", user.getUsername());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        contentValues.put("dateAdded", formatter.format(date));

        this.database.insert("Wishlist", null, contentValues);
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
