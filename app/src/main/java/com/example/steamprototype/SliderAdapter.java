package com.example.steamprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.entity.Game;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<Game> gameList;
    private final GameDataStorage gameDataStorage = MainActivity.gameDataStorage;

    // Constructor
    public SliderAdapter(ArrayList<Game> list) {
        this.gameList = list;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        Game game = gameList.get(position);
        viewHolder.imageView.setImageBitmap(gameDataStorage.getGameImage(game.getImage()));
        viewHolder.gameName.setText(game.getTitle());
        viewHolder.gameDiscount.setText(getDiscountTag(game.getDiscount()));
        viewHolder.gamePrice.setText(getPriceTag(game.getPrice()));
    }

    private String getDiscountTag(double discount) {
        return discount == 0.0 ? "" : "-" + discount * 100 + "%";
    }

    private String getPriceTag(double price) {
        return price == 0.0 ? "Free" : "Price: $" + String.format("%.2f", price);
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
