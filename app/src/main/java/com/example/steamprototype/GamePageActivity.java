package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.entity.Game;

public class GamePageActivity extends AppCompatActivity {
    ImageView gameImage;
    TextView gameName, gameDev, gamePub, gameDate, gameGenre, gameDiscount, gamePrice;
    Button btnBuy, btnWish;
    Game game;

    GameDataStorage gameDataStorage = MainActivity.gameDataStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        innit();

        Intent intent = getIntent();
        this.game = (Game) intent.getSerializableExtra("game");
        this.gameImage.setImageBitmap(gameDataStorage.getGameImage(game.getImage()));
        this.gameName.setText(game.getTitle());
        this.gamePub.setText(game.getPublisher());
        this.gameDev.setText(game.getDeveloper());
        this.gameGenre.setText(game.getGenre());
        this.gameDate.setText(game.getReleaseDateString());
        this.gamePrice.setText(game.getPriceString());
        this.gameDiscount.setText(game.getDiscountString());
    }

    public void innit() {
        this.gameImage = findViewById(R.id.img_page);
        this.gameName = findViewById(R.id.txtV_page_name);
        this.gameDev = findViewById(R.id.txtV_page_dev);
        this.gamePub = findViewById(R.id.txtV_page_publisher);
        this.gameDate = findViewById(R.id.txtV_page_date);
        this.gameGenre = findViewById(R.id.txtV_page_genre);
        this.gameDiscount = findViewById(R.id.txtV_page_discount);
        this.gamePrice = findViewById(R.id.txtV_page_price);

        this.btnBuy = findViewById(R.id.btnBuy);
        this.btnWish = findViewById(R.id.btnWish);
    }
}
