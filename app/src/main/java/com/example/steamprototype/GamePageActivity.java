package com.example.steamprototype;

import static com.example.steamprototype.StoreFrontActivity.localizeGameAttribute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.LocalizedGame;
import com.example.steamprototype.entity.User;

import java.text.NumberFormat;

public class GamePageActivity extends AppCompatActivity implements RateGameFragment.OnRateListener {
    ImageView gameImage;
    TextView gameName, gameDev, gamePub, gameDate, gameGenre, gameDiscount, gamePrice, gameDesc;
    Button btnBuy, btnWish, btnRate;
    Game game;
    User user;
    GameDataStorage gameDataStorage = MainActivity.gameDataStorage;
    UserLibraryStorage userLibraryStorage = StoreFrontActivity.userLibraryStorage;

    private boolean isFragmentDisplayed = false, gameOwned = false;
    private double rate = 0.0;
    static final String STATE_FRAGMENT = "state_of_fragment";

    private NumberFormat numberFormat = NumberFormat.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        innit();

        Intent intent = getIntent();
        this.game = (Game) intent.getSerializableExtra("game");
        this.user = (User) intent.getSerializableExtra("buyingUser");

        if (this.user == null) {
            this.user = (User) intent.getSerializableExtra("user");
        }

        if (userLibraryStorage.checkContainLib(this.user, this.game)) {
            btnBuy.setVisibility(View.INVISIBLE);
            btnWish.setVisibility(View.INVISIBLE);
            btnBuy.setEnabled(false);
            btnWish.setEnabled(false);
            gameOwned = true;
        }

        if (userLibraryStorage.checkContainWish(this.user, this.game)) {
            btnWish.setVisibility(View.INVISIBLE);
            btnWish.setEnabled(false);
        }

        this.gameImage.setImageBitmap(gameDataStorage.getGameImage(game.getImage()));
        this.gameName.setText(game.getTitle());
        this.gamePub.setText(game.getPublisher());
        this.gameDev.setText(game.getDeveloper());
        this.gameGenre.setText(game.getGenre());
        this.gameDate.setText(game.getReleaseDateString());
        this.gameDiscount.setText(game.getDiscountString());

        SharedPreferences sharedPref = getSharedPreferences("Language",0);
        String countryCode = sharedPref.getString("lang_code", "en");
        LocalizedGame localizedGame = localizeGameAttribute.getLocalizedGame(countryCode, game.getGameID());

        this.gamePrice.setText(localizedGame.getConvertedPrice());
        this.gameDesc.setText(localizedGame.getTranslatedDesc());

        this.gameDesc.setMovementMethod(new ScrollingMovementMethod());

        btnBuy.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                intent.putExtra("bought", game);
                setResult(StoreFrontActivity.RESULT_CODE_BOUGHT, intent);
                finish();
            });
            builder.setNegativeButton(getString(R.string.no), (dialog, id) -> {

            });
            builder.setMessage(getString(R.string.buy_confirm_message));
            builder.setTitle(getString(R.string.buy_confirm_title));
            builder.setIcon(R.drawable.steamdeck_steamlogo);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        btnWish.setOnClickListener(v -> {
            intent.putExtra("wish", this.game);
            setResult(StoreFrontActivity.RESULT_CODE_ADD_TO_WISHLIST, intent);
            finish();
        });

        String ratingText = getString(R.string.rate_button) +
                " " + game.getRatingString() +
                "★" + " (" +
                numberFormat.format(game.getRatingCount()) + ")";

        btnRate.setText(ratingText);

        btnRate.setOnClickListener(v -> {
            if (gameOwned) {
                if (game.isRated() && !isFragmentDisplayed) {
                    Toast.makeText(this, getString(R.string.rating_condition_message2), Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("Ratings",MODE_PRIVATE);
                    boolean isRated = sharedPreferences.getBoolean("" + game.getGameID(), false);
                    if (isFragmentDisplayed && !isRated) {
                        closeFragment();
                    } else {
                        displayFragment();
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.rating_condition_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void displayFragment() {
        RateGameFragment rateGameFragment = RateGameFragment.newInstance(rate);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, rateGameFragment).addToBackStack(null).commit();
        isFragmentDisplayed = true;
    }

    public void closeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RateGameFragment rateGameFragment = (RateGameFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (rateGameFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(rateGameFragment).commit();
        }
        isFragmentDisplayed = false;
    }

    @Override
    public void onRateChoice(double rate) {
        int id = game.getGameID();
        game.setRated();
        game.addRating(rate);
        gameDataStorage.updateGameRating(id, game.getTotalRate(), game.getRatingCount());
        Toast.makeText(this, getString(R.string.rating_thank_message),  Toast.LENGTH_SHORT).show();
        updateRating();
        SharedPreferences sharedPreferences = getSharedPreferences("Ratings",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("" + id,true).apply();
        closeFragment();
    }

    public void updateRating() {
        String ratingText = getString(R.string.rate_button) +
                " " + game.getRatingString() +
                "★" + " (" +
                numberFormat.format(game.getRatingCount()) + ")";

        btnRate.setText(ratingText);
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
        this.gameDesc = findViewById(R.id.txtV_page_description);
        this.btnBuy = findViewById(R.id.btnBuy);
        this.btnWish = findViewById(R.id.btnWish);
        this.btnRate = findViewById(R.id.btnRate);
    }
}
