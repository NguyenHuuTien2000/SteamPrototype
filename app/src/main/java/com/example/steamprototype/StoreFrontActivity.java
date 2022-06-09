package com.example.steamprototype;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.entity.Game;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class StoreFrontActivity extends AppCompatActivity {
    SliderView sliderView;
    Button btn_search, btn_new, btn_popular, btn_special;
    EditText edt_search;
    ListView listView;
    ListViewAdapter listViewAdapter;
    SliderAdapter sliderAdapter;
    ArrayList<Game> gameArrayList = new ArrayList<>();
    int[] img = {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storefront_activity);

        init();

        sliderAdapter = new SliderAdapter(this,gameArrayList,img);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        listViewAdapter = new ListViewAdapter(this,gameArrayList);
        listView.setAdapter(listViewAdapter);




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
