package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steamprototype.adapter.ListViewAdapter;
import com.example.steamprototype.adapter.SliderAdapter;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Locale;

public class WishListActivity extends AppCompatActivity {
    Button btn_search, btn_all;
    EditText edt_search;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Game> gameArrayList;
    User user;
    UserLibraryStorage userLibraryStorage = StoreFrontActivity.userLibraryStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_activity);
        init();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        gameArrayList = (ArrayList<Game>) this.user.getWishlist();

        loadListView(this.gameArrayList);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            gotoGamePage(position);
        });

        btn_search.setOnClickListener(v -> {
            String text = edt_search.getText().toString().toLowerCase(Locale.ROOT).trim();
            if (text.length() == 0) {
                loadListView(this.gameArrayList);
            } else {
                ArrayList<Game> filtered = new ArrayList<>();
                for (Game game : this.gameArrayList) {
                    if (game.getTitle().toLowerCase(Locale.ROOT).startsWith(text)) {
                        filtered.add(game);
                    }
                }
                loadListView(filtered);
            }
        });

        btn_all.setOnClickListener(v -> {
            loadListView(this.gameArrayList);
        });
    }

    public void loadListView(ArrayList<Game> displayList) {
        this.listViewAdapter = new ListViewAdapter(WishListActivity.this, displayList);
        this.listView.setAdapter(this.listViewAdapter);
    }

    public void init() {
        btn_search = (Button) findViewById(R.id.btn_wishsearch);
        edt_search = (EditText) findViewById(R.id.edit_wishsearch);
        listView = (ListView) findViewById(R.id.lstVWL);
        btn_all = findViewById(R.id.btn_wishAll);
    }
    public void gotoGamePage(int pos) {
        Game game = gameArrayList.get(pos);
        Intent intent = new Intent(WishListActivity.this, GamePageActivity.class);
        intent.putExtra("user", this.user);
        intent.putExtra("game", game);
        startActivityForResult(intent, StoreFrontActivity.REQUEST_CODE_BUY);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StoreFrontActivity.REQUEST_CODE_BUY) {
            if (resultCode == StoreFrontActivity.RESULT_CODE_BOUGHT) {
                Game game = (Game) data.getSerializableExtra("bought");
                this.userLibraryStorage.addGameToLibrary(this.user, game);
                Toast.makeText(this, game.getTitle() + " added to your library", Toast.LENGTH_LONG).show();
            }
        }
    }
}
