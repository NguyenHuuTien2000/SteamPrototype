package com.example.steamprototype.adapter;

import static com.example.steamprototype.StoreFrontActivity.localizeGameAttribute;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.steamprototype.MainActivity;
import com.example.steamprototype.R;
import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.LocalizedGame;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Game> gameList;
    ArrayList<Game> tempList;

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
            lv_row.ratings = (TextView) convertView.findViewById(R.id.lsttV_txtV_rating);

            convertView.setTag(lv_row);

        } else {
            lv_row = (ListView_Row) convertView.getTag();
        }

        Game game = gameList.get(position);

        lv_row.img.setImageBitmap(gameDataStorage.getGameImage(game.getImage()));
        lv_row.gameName.setText(game.getTitle());
        lv_row.genre.setText(game.getGenre());
        lv_row.discount.setText(game.getDiscountString());

        String ratingText = game.getRatingString() + "★";
        lv_row.ratings.setText(ratingText);

        SharedPreferences sharedPref = context.getSharedPreferences("Language",0);
        String countryCode = sharedPref.getString("lang_code", "en");
        LocalizedGame localizedGame = localizeGameAttribute.getLocalizedGame(countryCode, game.getGameID());

        lv_row.price.setText(localizedGame.getConvertedPrice());

        return convertView;
    }

    private class ListView_Row{
        public ImageView img;
        public TextView gameName;
        public TextView genre;
        public TextView discount;
        public TextView price;

        public TextView ratings;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                gameList = (ArrayList<Game>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Game> FilteredArrList = new ArrayList<>();

                if (tempList == null) {
                    tempList = new ArrayList<>(gameList);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = tempList.size();
                    results.values = tempList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < tempList.size(); i++) {
                        String data = tempList.get(i).getTitle();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(tempList.get(i));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
