package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

public class WishListActivity extends AppCompatActivity {
    Button btn_search;
    EditText edt_search;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Game> gameArrayList;
    User user;
    UserLibraryStorage userLibraryStorage;
    public static final int REQUEST_CODE_BUY = 30;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_activity);
        init();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        listViewAdapter = new ListViewAdapter(WishListActivity.this, gameArrayList);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            gotoGamePage(position);
        });
    }
    public void init() {
        btn_search = (Button) findViewById(R.id.btn_wishsearch);
        edt_search = (EditText) findViewById(R.id.edit_wishsearch);
        listView = (ListView) findViewById(R.id.lstWishlist);
    }
    public void gotoGamePage(int pos) {
        Game game = gameArrayList.get(pos);
        Intent buyIntent = new Intent(WishListActivity.this, GamePageActivity.class);
        buyIntent.putExtra("game", game);
        startActivityForResult(buyIntent, REQUEST_CODE_BUY);
    }
}
