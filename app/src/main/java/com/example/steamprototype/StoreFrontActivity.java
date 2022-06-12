package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.adapter.ListViewAdapter;
import com.example.steamprototype.adapter.SliderAdapter;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Locale;

public class StoreFrontActivity extends AppCompatActivity {
    SliderView sliderView;
    Button btn_search, btn_new, btn_popular, btn_special;
    EditText edt_search;
    ListView listView;
    ListViewAdapter listViewAdapter;
    SliderAdapter sliderAdapter;

    ArrayList<Game> gameArrayList;
    ArrayList<Game> wishList;

    User user;
    public static UserLibraryStorage userLibraryStorage;

    public static final int REQUEST_CODE_BUY = 30;
    public static final int RESULT_CODE_BOUGHT = 31;
    public static final int RESULT_CODE_ADD_TO_WISHLIST = 32;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.mn_store) {
//            goToStore();
//        }
        if (id == R.id.mn_lib) {
            goToLibrary();
        }
        if (id == R.id.mn_wlst) {
            goToWishList();
        }
//        if (id == R.id.mn_prof) {
//            goToProfile();
//        }
        if (id == R.id.mn_out) {
            logOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storefront_activity);

        init();

        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        this.gameArrayList = MainActivity.gameDataStorage.getGameList();

        userLibraryStorage = new UserLibraryStorage(this.gameArrayList, this.wishList);
        userLibraryStorage.loadUserLists(this.user);

        sliderAdapter = new SliderAdapter(gameArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        loadListView(this.gameArrayList);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            gotoGamePage(position);
        });

        sliderAdapter.setOnItemClickListener(this::gotoGamePage);

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
    }

    public void loadListView(ArrayList<Game> displayList) {
        this.listViewAdapter = new ListViewAdapter(StoreFrontActivity.this, displayList);
        this.listView.setAdapter(this.listViewAdapter);
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
        if (resultCode == RESULT_CODE_ADD_TO_WISHLIST) {
            Game game = (Game) data.getSerializableExtra("wishlist");
            this.userLibraryStorage.addGameToLibrary(this.user, game);
            Toast.makeText(this, game.getTitle() + " added to your wishlist\n Any discount will be notified through email", Toast.LENGTH_LONG).show();
        }
    }

    public void gotoGamePage(int pos) {
        Game game = gameArrayList.get(pos);
        Intent buyIntent = new Intent(StoreFrontActivity.this, GamePageActivity.class);
        buyIntent.putExtra("buyingUser", this.user);
        buyIntent.putExtra("game", game);
        startActivityForResult(buyIntent, REQUEST_CODE_BUY);
    }

//    public void goToStore( ) {
//        startActivity(new Intent(this, StoreFrontActivity.class));
//    }

    public void goToLibrary( ) {
        Intent goIntent = new Intent(this, LibraryActivity.class);
        goIntent.putExtra("user", this.user);
        startActivity(goIntent);
    }
//
//    public void goToProfile(){
//    }
//

    public void goToWishList(){
        Intent goIntent = new Intent(this, WishListActivity.class);
        goIntent.putExtra("user", this.user);
        startActivity(goIntent);
    }

    public void logOut() {
        finish();
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
