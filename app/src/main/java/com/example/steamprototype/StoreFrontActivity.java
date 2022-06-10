package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.adapter.ListViewAdapter;
import com.example.steamprototype.adapter.SliderAdapter;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class StoreFrontActivity extends AppCompatActivity {
    SliderView sliderView;
    Button btn_search, btn_new, btn_popular, btn_special;
    EditText edt_search;
    ListView listView;
    ListViewAdapter listViewAdapter;
    SliderAdapter sliderAdapter;
    ArrayList<Game> gameArrayList;

    User user;
    UserLibraryStorage userLibraryStorage;

    public static final int REQUEST_CODE_BUY = 30;
    public static final int RESULT_CODE_BOUGHT = 31;
    public static final int RESULT_CODE_ADD_TO_WISHLIST = 32;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storefront_activity);

        init();

        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        this.gameArrayList = MainActivity.gameDataStorage.getGameList();
        this.userLibraryStorage = new UserLibraryStorage(this.gameArrayList);
        this.userLibraryStorage.loadUserLibrary(this.user);

        sliderAdapter = new SliderAdapter(gameArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        listViewAdapter = new ListViewAdapter(StoreFrontActivity.this, gameArrayList);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            gotoGamePage(position);
        });

        sliderAdapter.setOnItemClickListener(this::gotoGamePage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BUY) {
            if (resultCode == RESULT_CODE_BOUGHT) {
                Game game = (Game) data.getSerializableExtra("bought");
                this.userLibraryStorage.addGameToLibrary(this.user, game);
                Toast.makeText(this, game.getTitle() + " added to your library", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void gotoGamePage(int pos) {
        Game game = gameArrayList.get(pos);
        Intent buyIntent = new Intent(StoreFrontActivity.this, GamePageActivity.class);
        buyIntent.putExtra("game", game);
        startActivityForResult(buyIntent, REQUEST_CODE_BUY);
    }

    public void init() {
        sliderView = (SliderView) findViewById(R.id.slider);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_new = (Button) findViewById(R.id.btn_new);
        btn_popular = (Button) findViewById(R.id.btn_popular);
        btn_special = (Button) findViewById(R.id.btn_special);
        edt_search = (EditText) findViewById(R.id.edt_search);
        listView = (ListView) findViewById(R.id.lstStore);
    }
}
