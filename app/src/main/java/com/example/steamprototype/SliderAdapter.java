package com.example.steamprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.steamprototype.entity.Game;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    int[] imgList;
    private List<Game> gameList;

    // Constructor
    public SliderAdapter(Context context, ArrayList<Game> list, int[] imgList) {
        this.gameList = list;
        this.imgList = imgList;
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
        viewHolder.imageView.setImageResource(imgList[position]);;
        viewHolder.gameName.setText(gameList.get(position).getTitle());
        viewHolder.gameDiscount.setText("" + gameList.get(position).getDiscount()*100);
        viewHolder.gamePrice.setText(String.format("%.3f",gameList.get(position).getPrice()));
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
            imageView = itemView.findViewById(R.id.sldV_txtV_sliderVprice);
            gameName = itemView.findViewById(R.id.sldV_txtV_nameGame);
            gameDiscount = itemView.findViewById(R.id.sldV_txtV_sliderVdiscount);
            gamePrice = itemView.findViewById(R.id.sldV_txtV_sliderVprice);
            this.itemView = itemView;
        }
    }
}
