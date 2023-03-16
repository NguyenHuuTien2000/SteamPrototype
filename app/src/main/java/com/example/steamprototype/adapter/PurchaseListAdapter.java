package com.example.steamprototype.adapter;

import static com.example.steamprototype.StoreFrontActivity.localizeGameAttribute;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.R;
import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.LocalizedGame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PurchaseListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Game> gameList;
    GameDataStorage gameDataStorage = MainActivity.gameDataStorage;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public PurchaseListAdapter(Context context, ArrayList<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PurchaseListRow row;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.purchase_history_list_item, null);

            row = new PurchaseListRow();
            row.gameName = view.findViewById(R.id.purchased_game_name);
            row.dateAdded = view.findViewById(R.id.purchased_game_date);
            row.price = view.findViewById(R.id.purchased_game_price);

            view.setTag(row);
        } else {
            row = (PurchaseListRow) view.getTag();
        }

        Game game = gameList.get(i);

        row.gameName.setText(game.getTitle());

        row.dateAdded.setText(formatter.format(game.getDateAdded()));

        SharedPreferences sharedPref = context.getSharedPreferences("Language",0);
        String countryCode = sharedPref.getString("lang_code", "en");
        LocalizedGame localizedGame = localizeGameAttribute.getLocalizedGame(countryCode, game.getGameID());

        row.price.setText(localizedGame.getConvertedPrice());

        return view;
    }

    private class PurchaseListRow {
        public TextView gameName;
        public TextView dateAdded;
        public TextView price;
    }
}
