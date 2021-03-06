package com.example.steamprototype.data_op;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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

        query = "CREATE TABLE IF NOT EXISTS wishlist ( " +
                "username VARCHAR(20) REFERENCES user(username)," +
                "gameID INTEGER REFERENCES game(gameID)," +
                "dateAdded DATETIME NOT NULL," +
                "discounted BOOL NOT NULL )";
        database.execSQL(query);
    }

    public boolean checkContainLib(User user, Game game) {
        List<Game> library = user.getLibrary();
        for (Game libGame : library) {
            if (game.getGameID() == libGame.getGameID()) {
                return true;
            }
        }
        Cursor cursor = database.rawQuery("SELECT * FROM library WHERE username == '" + user.getUsername() + "'", null);
        while (cursor.moveToNext()) {
            if (game.getGameID() == cursor.getInt(1)) {
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public boolean checkContainWish(User user, Game game) {
        List<Game> wishlist = user.getWishlist();
        for (Game wishGame : wishlist) {
            if (game.getGameID() == wishGame.getGameID()) {
                return true;
            }
        }
        Cursor cursor = database.rawQuery("SELECT * FROM wishlist WHERE username == '" + user.getUsername() + "'", null);
        while (cursor.moveToNext()) {
            if (game.getGameID() == cursor.getInt(1)) {
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public boolean addGameToLibrary(User user, Game game) {
        if (checkContainLib(user, game)) {
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


    public void loadUserLists(User user) {
        Cursor cursor = database.rawQuery("SELECT * FROM library WHERE username == '" + user.getUsername() + "'", null);
        while (cursor.moveToNext()) {
            int gameID = Integer.parseInt(cursor.getString(1));
            user.addToLibrary(fullList.get(gameID));
        }
        cursor.close();

        cursor = database.rawQuery("SELECT * FROM wishlist WHERE username == '" + user.getUsername() + "'", null);
        while (cursor.moveToNext()) {
            int gameID = Integer.parseInt(cursor.getString(1));
            user.addToWishlist(fullList.get(gameID));
        }
        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Game> getPopularList() {
        ArrayList<Game> popularGames = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT gameID, COUNT(gameID) FROM library GROUP BY gameID", null);
        while (cursor.moveToNext()) {
            Game game = fullList.get(cursor.getInt(0));
            game.setPopularity(cursor.getInt(1));
            popularGames.add(game);
        }
        popularGames.sort((o1, o2) -> o2.getPopularity() - o1.getPopularity());
        return popularGames;
    }
}
