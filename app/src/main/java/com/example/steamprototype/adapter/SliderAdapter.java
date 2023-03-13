package com.example.steamprototype.adapter;

import static com.example.steamprototype.StoreFrontActivity.localizeGameAttribute;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steamprototype.GamePageActivity;
import com.example.steamprototype.MainActivity;
import com.example.steamprototype.R;
import com.example.steamprototype.StoreFrontActivity;
import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.LocalizedGame;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<Game> gameList;
    private final GameDataStorage gameDataStorage = MainActivity.gameDataStorage;
    private SliderAdapter.OnItemClickListener listener;

    private Context context;

    // Constructor
    public SliderAdapter(Context context, ArrayList<Game> list) {
        this.context = context;
        this.gameList = list;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderAdapterViewHolder(inflate);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // lets create the set onclick method
    public void setOnItemClickListener(SliderAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        Game game = gameList.get(position);
        viewHolder.imageView.setImageBitmap(gameDataStorage.getGameImage(game.getImage()));
        viewHolder.gameName.setText(game.getTitle());
        viewHolder.gameDiscount.setText(game.getDiscountString());

        SharedPreferences sharedPref = context.getSharedPreferences("Language",0);
        String countryCode = sharedPref.getString("lang_code", "en");
        LocalizedGame localizedGame = localizeGameAttribute.getLocalizedGame(countryCode, game.getGameID());

        viewHolder.gamePrice.setText(localizedGame.getConvertedPrice());

        viewHolder.itemView.setOnClickListener(v -> {
            if (this.listener != null) {
                this.listener.onItemClick(position);
            }
        });
    }

    // this method will return
    // the count of our list.
    public int getCount() {
        return gameList.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageView;
        TextView gameName, gamePrice, gameDiscount;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgV_imgGameSlider);
            gameName = itemView.findViewById(R.id.sldV_txtV_nameGame);
            gameDiscount = itemView.findViewById(R.id.sldV_txtV_sliderVdiscount);
            gamePrice = itemView.findViewById(R.id.sldV_txtV_sliderVprice);
            this.itemView = itemView;
        }
    }
}
