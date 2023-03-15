package com.example.steamprototype;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.adapter.ListViewAdapter;
import com.example.steamprototype.adapter.SliderAdapter;
import com.example.steamprototype.data_op.LocalizeGameAttribute;
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

    public static LocalizeGameAttribute localizeGameAttribute;

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
        if (id == R.id.mn_prof) {
            goToProfile();
        }
        if (id == R.id.mn_set) {
            goToSettings();
        }

        if (id == R.id.mn_out) {
            logOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private static boolean change = false;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("Language",0);
        if (sharedPreferences.getBoolean("lang_change",false)) {
            change = true;
            MainActivity.settingMethods.setLanguage(StoreFrontActivity.this);
            sharedPreferences.edit().putBoolean("lang_change", false).apply();
            recreate();
        }
        this.gameArrayList = MainActivity.gameDataStorage.getGameList();
        loadListView(this.gameArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.settingMethods.setLanguage(StoreFrontActivity.this);
        setContentView(R.layout.storefront_activity);

        init();

        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        this.gameArrayList = MainActivity.gameDataStorage.getGameList();

        localizeGameAttribute = new LocalizeGameAttribute(this.gameArrayList);
        userLibraryStorage = new UserLibraryStorage(this.gameArrayList, this.wishList);
        if (!change) {
            userLibraryStorage.loadUserLists(this.user);
        }

        sliderAdapter = new SliderAdapter(this, gameArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        loadListView(this.gameArrayList);

        sliderAdapter.setOnItemClickListener(position -> gotoGamePage(position, this.gameArrayList));

        btn_search.setOnClickListener(v -> {

            String text = edt_search.getText().toString().toLowerCase(Locale.ROOT).trim();
            if (text.length() == 0) {
                loadListView(this.gameArrayList);
            } else {
                ArrayList<Game> filtered = new ArrayList<>();
                for (Game game : this.gameArrayList) {
                    if (game.getTitle().toLowerCase(Locale.ROOT).contains(text)) {
                        filtered.add(game);
                    }
                }
                loadListView(filtered);
            }
            hideKeyboard(this);
        });

        btn_new.setOnClickListener(v -> {
            ArrayList<Game> sorted = new ArrayList<>(this.gameArrayList);
            sorted.sort((o1, o2) -> o2.getReleaseDate().compareTo(o1.getReleaseDate()));
            loadListView(sorted);
        });

        btn_popular.setOnClickListener(v -> {
            loadListView(userLibraryStorage.getPopularList());
        });

        btn_special.setOnClickListener(v -> {
            loadListView(this.gameArrayList);
        });

        edt_search.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (keyEvent != null&& (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard(StoreFrontActivity.this);
                btn_search.performClick();
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BUY) {
            if (resultCode == RESULT_CODE_BOUGHT) {
                Game game = (Game) data.getSerializableExtra("bought");
                userLibraryStorage.addGameToLibrary(this.user, game);
                Toast.makeText(this, game.getTitle() + " added to your library", Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode == RESULT_CODE_ADD_TO_WISHLIST) {
            Game game = (Game) data.getSerializableExtra("wish");
            userLibraryStorage.addGameToWL(this.user, game);
            Toast.makeText(this, game.getTitle() + " added to your wishlist\n Any discount will be notified through email", Toast.LENGTH_LONG).show();
        }
    }

    public void loadListView(ArrayList<Game> displayList) {
        this.listViewAdapter = new ListViewAdapter(StoreFrontActivity.this, displayList);
        this.listView.setAdapter(this.listViewAdapter);
        this.listView.setOnItemClickListener((parent, view, position, id) -> {
            gotoGamePage(position, displayList);
        });
    }

    public void gotoGamePage(int pos, ArrayList<Game> selectedList) {
        Game game = selectedList.get(pos);
        Intent buyIntent = new Intent(StoreFrontActivity.this, GamePageActivity.class);
        buyIntent.putExtra("buyingUser", this.user);
        buyIntent.putExtra("game", game);
        startActivityForResult(buyIntent, REQUEST_CODE_BUY);
    }

//    public void goToStore( ) {
//        startActivity(new Intent(this, StoreFrontActivity.class));
//    }

    public void goToLibrary( ) {
        Intent goIntent = new Intent(StoreFrontActivity.this, LibraryActivity.class);
        goIntent.putExtra("user", this.user);
        startActivity(goIntent);
    }

    public void goToProfile(){
        Intent goIntent = new Intent(StoreFrontActivity.this, ProfileActivity.class);
        goIntent.putExtra("user", this.user);
        startActivity(goIntent);
    }

    private void goToSettings() {
        Intent goIntent = new Intent(StoreFrontActivity.this, SettingsActivity.class);
        startActivity(goIntent);
    }

    public void goToWishList(){
        Intent goIntent = new Intent(StoreFrontActivity.this, WishListActivity.class);
        goIntent.putExtra("user", this.user);
        startActivity(goIntent);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void logOut() {
        finish();
    }

    public void init() {
        sliderView = findViewById(R.id.slider);
        btn_search = findViewById(R.id.btn_search);
        btn_new = findViewById(R.id.btn_new);
        btn_popular = findViewById(R.id.btn_popular);
        btn_special = findViewById(R.id.btn_special);
        edt_search = findViewById(R.id.edt_search);
        listView = findViewById(R.id.lstStore);
    }
}
