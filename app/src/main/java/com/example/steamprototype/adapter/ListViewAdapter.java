package com.example.steamprototype.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.R;
import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.entity.Game;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Game> gameList;
    GameDataStorage gameDataStorage = MainActivity.gameDataStorage;

    public ListViewAdapter(Context context, ArrayList<Game> gameList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ListView_Row lv_row;

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_item,null);

            lv_row = new ListView_Row();
            lv_row.img = (ImageView) convertView.findViewById(R.id.lsttV_img_gameImg);
            lv_row.gameName = (TextView) convertView.findViewById(R.id.lsttV_txtV_gameName);
            lv_row.genre = (TextView) convertView.findViewById(R.id.lsttV_txtV_genre);
            lv_row.discount = (TextView) convertView.findViewById(R.id.lsttV_txtV_discount);
            lv_row.price = (TextView) convertView.findViewById(R.id.lsttV_txtV_price);

            convertView.setTag(lv_row);

        } else {
            lv_row = (ListView_Row) convertView.getTag();
        }

        Game game = gameList.get(position);

        lv_row.img.setImageBitmap(gameDataStorage.getGameImage(game.getImage()));
        lv_row.gameName.setText(game.getTitle());
        lv_row.genre.setText(game.getGenre());
        lv_row.discount.setText(game.getDiscountString());
        lv_row.price.setText(game.getPriceString());

        return convertView;
    }

    private class ListView_Row{
        public ImageView img;
        public TextView gameName;
        public TextView genre;
        public TextView discount;
        public TextView price;
    }

}
