package com.example.steamprototype.network;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConversionAPI extends AsyncTask<Void, Void, String> {
    private OnConversionCompleteListener listener;

    @Override
    protected String doInBackground(Void... voids) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();

            Request request = new Request.Builder()
                    .url("https://api.apilayer.com/fixer/latest?symbols=JPY%2C%20CNY%2C%20VND%2C%20EUR&base=USD")
                    .addHeader("apikey", "GQJhy9mEXtchlY0ZNlA4t2fT9CE6TPrH")
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String text) {
        listener.onCompleted(text);
    }
    public interface OnConversionCompleteListener{
        void onCompleted(String text);
        void onError(Exception e);
    }
    public void setListener(OnConversionCompleteListener listener){
        this.listener=listener;
    }
}
