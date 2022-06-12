package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;

public class GamePageActivity extends AppCompatActivity {
    ImageView gameImage;
    TextView gameName, gameDev, gamePub, gameDate, gameGenre, gameDiscount, gamePrice;
    Button btnBuy, btnWish;
    Game game;
    User user;
    GameDataStorage gameDataStorage = MainActivity.gameDataStorage;
    UserLibraryStorage userLibraryStorage = StoreFrontActivity.userLibraryStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        innit();

        Intent intent = getIntent();
        this.game = (Game) intent.getSerializableExtra("game");
        this.user = (User) intent.getSerializableExtra("buyingUser");

        if (userLibraryStorage.checkContain(this.user, this.game)) {
            btnBuy.setVisibility(View.INVISIBLE);
            btnWish.setVisibility(View.INVISIBLE);
            btnBuy.setEnabled(false);
            btnWish.setEnabled(false);
        }

        this.gameImage.setImageBitmap(gameDataStorage.getGameImage(game.getImage()));
        this.gameName.setText(game.getTitle());
        this.gamePub.setText("Publisher:\t" + game.getPublisher());
        this.gameDev.setText("Developer:\t" + game.getDeveloper());
        this.gameGenre.setText("Genre:\t" + game.getGenre());
        this.gameDate.setText("Release Date:\t" + game.getReleaseDateString());
        this.gamePrice.setText(game.getPriceString());
        this.gameDiscount.setText(game.getDiscountString());

        btnBuy.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", (dialog, id) -> {
                intent.putExtra("bought", game);
                setResult(StoreFrontActivity.RESULT_CODE_BOUGHT, intent);
                finish();
            });
            builder.setNegativeButton("No", (dialog, id) -> {

            });
            builder.setMessage("Do you want to buy this game?");
            builder.setTitle("Confirm order");
            builder.setIcon(R.drawable.steamdeck_steamlogo);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
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
