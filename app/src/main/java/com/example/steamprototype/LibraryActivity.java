package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.adapter.ListViewAdapter;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;

import java.util.ArrayList;
import java.util.Locale;

public class LibraryActivity  extends AppCompatActivity {
    Button btn_search;
    EditText edt_search;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Game> gameArrayList;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity);
        init();
        Intent intent = getIntent();

        this.user = (User) intent.getSerializableExtra("user");
        this.gameArrayList = (ArrayList<Game>) this.user.getLibrary();

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


    }

    public void loadListView(ArrayList<Game> displayList) {
        this.listViewAdapter = new ListViewAdapter(LibraryActivity.this, displayList);
        this.listView.setAdapter(this.listViewAdapter);
    }

    public void init() {
        btn_search = findViewById(R.id.btn_librarysrch);
        edt_search = findViewById(R.id.edit_librarysrch);
        listView = findViewById(R.id.lstVLib);
    }
    public void gotoGamePage(int pos) {
        Game game = gameArrayList.get(pos);
        Intent intent = new Intent(LibraryActivity.this, GamePageActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }
}
