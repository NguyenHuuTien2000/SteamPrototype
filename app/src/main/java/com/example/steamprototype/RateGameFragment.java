package com.example.steamprototype;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RateGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RateGameFragment extends Fragment {

    private static double rate = 0.0;

    private static final String RATE = "rate";

    OnRateListener listener;

    public RateGameFragment() {
        // Required empty public constructor
    }

    public static RateGameFragment newInstance(double rate) {
        RateGameFragment fragment = new RateGameFragment();
        Bundle args = new Bundle();
        args.putDouble(RATE, rate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rate_game, container, false);
        RatingBar ratingBar = rootView.findViewById(R.id.rating_bar);
//        if (getArguments().containsKey(RATE)) {
//            rate = getArguments().getDouble(RATE);
//            SharedPreferences sharedPreferences =
//        }

        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) -> {
            rate = ratingBar1.getRating();
            listener.onRateChoice(rate);
        });

        return  rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRateListener) {
            listener = (OnRateListener) context;
        } else {
            throw new ClassCastException();
        }
    }

    interface OnRateListener {
        void onRateChoice(double rate);
    }

}