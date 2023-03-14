package com.example.steamprototype.data_op;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.entity.Game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GameDataStorage {

    private SQLiteDatabase database;
    private ArrayList<Game> gameLists;


    public GameDataStorage(Activity activity) {
        this.database = MainActivity.userDataStorage.getDatabase();

        if (MainActivity.userDataStorage.checkFirstRun()) {
            this.createTable();
            this.seedGameData(activity);
        } else {
            loadAllGameFromDB();
        }
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS game( " +
                "gameID INTEGER PRIMARY KEY NOT NUll UNIQUE," +
                "title VARCHAR(60) NOT NUll UNIQUE," +
                "publisher VARCHAR(30) NOT NUll," +
                "developer VARCHAR(30) NOT NUll," +
                "genre VARCHAR(40) NOT NUll," +
                "platform VARCHAR(40) NOT NUll," +
                "releaseDate DATETIME NOT NUll," +
                "price DOUBLE NOT NUll," +
                "discount DOUBLE NOT NUll," +
                "image TEXT NOT NUll UNIQUE," +
                "description TEXT," +
                "totalRate DOUBLE NOT NULL," +
                "rateCount LONG NOT NULL)";
        database.execSQL(query);
    }

    public ArrayList<Game> getGameList() {
        loadAllGameFromDB();
        return this.gameLists;
    }

    public void seedGameData(Activity activity) {
        Seedr seeder = new Seedr(activity);
        this.gameLists = seeder.start();
        for (Game game : this.gameLists) {
            insertGameToDB(game);
        }
    }

    public void insertGameToDB(Game game) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("gameID", game.getGameID());
        contentValues.put("title", game.getTitle());
        contentValues.put("publisher", game.getPublisher());
        contentValues.put("developer", game.getDeveloper());
        contentValues.put("genre", game.getGenre());
        contentValues.put("platform", game.getPlatform());
        contentValues.put("releaseDate", game.getReleaseDateFormatted());
        contentValues.put("price", game.getPrice());
        contentValues.put("discount", game.getDiscount());
        contentValues.put("image", game.getImage());
        contentValues.put("description", game.getDescription());
        contentValues.put("totalRate", game.getTotalRate());
        contentValues.put("rateCount", game.getRatingCount());
        this.database.insert("game", null, contentValues);
    }

    public void loadAllGameFromDB() {
        this.gameLists = new ArrayList<>();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor cursor = database.rawQuery("SELECT * FROM game", null);
        Game game;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String publisher = cursor.getString(2);
            String developer = cursor.getString(3);
            String genre = cursor.getString(4);
            String platform = cursor.getString(5);

            Date releaseDate = new Date();
            try {
                releaseDate = formatter.parse(cursor.getString(6));
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            double price = cursor.getDouble(7);
            double discount = cursor.getDouble(8);
            String imagePath = cursor.getString(9);
            String description = cursor.getString(10);
            double totalRate = cursor.getDouble(11);
            long rateCount = cursor.getLong(12);
            game = new Game(id, title, publisher, developer, genre, platform, releaseDate, price, discount, imagePath, totalRate, rateCount);
            game.setDescription(description);
            this.gameLists.add(game);
        }
        cursor.close();
    }

    public void updateGameRating(int id, double rate, long rateCount) {
        String query = "UPDATE game " +
                "SET totalRate = " + rate + "," +
                "    rateCount = " + rateCount + " " +
                "WHERE gameID = " + id;
        database.execSQL(query);
    }

    public Bitmap getGameImage(String path) {
        File imgFile = new  File(path);
        Bitmap bitmap = null;
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap;
    }
}
