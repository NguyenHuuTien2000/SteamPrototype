package com.example.steamprototype.data_op;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class UserLibraryStorage {
    private SQLiteDatabase database = MainActivity.userDataStorage.getDatabase();
    private List<Game> fullList;
    private List<Game> wishlist;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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


        Date date = new Date();
        contentValues.put("dateAdded", formatter.format(date));

        contentValues.put("discounted", game.getDiscount() > 0);

        this.database.insert("Wishlist", null, contentValues);
    }


    public void loadUserLists(User user) {

        //Get library data
        Cursor cursor = database.rawQuery("SELECT * FROM library WHERE username == '" + user.getUsername() + "'", null);
        while (cursor.moveToNext()) {
            int gameID = Integer.parseInt(cursor.getString(1));
            Game game = fullList.get(gameID);

            Date date = new Date();
            try {
                date = formatter.parse(cursor.getString(2));
            } catch (Exception e) {
                e.printStackTrace();
            }

            game.setDateAdded(date);
            user.addToLibrary(game);
        }
        cursor.close();

        //Get wishlist data
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
        //Cursor cursor = database.rawQuery("SELECT gameID, COUNT(gameID) FROM library GROUP BY gameID", null);
//        while (cursor.moveToNext()) {
//            Game game = fullList.get(cursor.getInt(0));
//            popularGames.add(game);
//        }
        popularGames.addAll(fullList);
        popularGames.sort((o1, o2) -> {
            int comp = Double.compare(o2.getRatingDouble(), o1.getRatingDouble());
            if (comp == 0) {
                comp = Long.compare(o2.getRatingCount(), o1.getRatingCount());
            }
            return comp;
        });
        return popularGames;
    }
}
