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

public class LibraryActivity  extends AppCompatActivity {
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
        setContentView(R.layout.library_activity);
        init();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");

        listViewAdapter = new ListViewAdapter(this, gameArrayList);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            gotoGamePage(position);
        });


    } public void init() {
        btn_search = (Button) findViewById(R.id.btn_librarysrch);
        edt_search = (EditText) findViewById(R.id.edit_librarysrch);
        listView = (ListView) findViewById(R.id.lstlibrary);
    }
    public void gotoGamePage(int pos) {
        Game game = gameArrayList.get(pos);
        Intent buyIntent = new Intent(LibraryActivity.this, GamePageActivity.class);
        buyIntent.putExtra("game", game);
        startActivityForResult(buyIntent, REQUEST_CODE_BUY);
    }
}
