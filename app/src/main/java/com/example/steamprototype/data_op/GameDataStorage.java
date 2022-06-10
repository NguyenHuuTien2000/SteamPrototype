package com.example.steamprototype.data_op;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.entity.Game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GameDataStorage {

    private SQLiteDatabase database;
    private ArrayList<Game> gameLists;

    public GameDataStorage(Activity activity) {
        this.database = MainActivity.userDataStorage.getDatabase();

        this.createTable();
        this.seedGameData(activity);
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
                "image TEXT NOT NUll UNIQUE )";
        database.execSQL(query);
    }

    public ArrayList<Game> getGameList() {
        return this.gameLists;
    }

    public void seedGameData(Activity activity) {

        this.gameLists = new ArrayList<>();
        Game game;
        Date date;
        String imagePath;

        //hell

        date = convertDate(2018, 6, 1);
        imagePath = createImageStorage(activity, "azur_lane.jpg");
        game = new Game(0, "Azure Lane", "Yostar", "Manjuu, Yongshi", "Strategy", "Mobile", date, 0, 0, imagePath);
        gameLists.add(game);
        insertGameToDB(game);

        date = convertDate(2015, 11, 3);
        imagePath = createImageStorage(activity, "nfs.jpg");
        game = new Game(1, "Need for Speed", "Electronic Arts", "Ghost Games", "Racing", "PC", date, 82, 10, imagePath);
        gameLists.add(game);
        insertGameToDB(game);

        date = convertDate(2017, 7, 5);
        imagePath = createImageStorage(activity, "cold_water.jpg");
        game = new Game(2, "Cold Waters", "Killerfish Games", "Killerfish Games", "Simulator", "PC", date, 40, 25, imagePath);
        gameLists.add(game);
        insertGameToDB(game);
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
        this.database.insert("game", null, contentValues);
    }

    public String createImageStorage(Activity activity, String imageName) {
        String path = "";
        try {
            String savePath = activity.getApplicationInfo().dataDir + "/images/";
            File file = new File(savePath + imageName);
            if (!file.exists()) {
                InputStream inputStream = activity.getAssets().open(imageName);
                File folder = new File(savePath);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(savePath + imageName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
            path = savePath + imageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public Bitmap getGameImage(String path) {
        File imgFile = new  File(path);
        Bitmap bitmap = null;
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap;
    }


    public Date convertDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.set(year, month, day);
        return cal.getTime();
    }
}
