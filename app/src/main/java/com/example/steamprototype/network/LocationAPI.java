package com.example.steamprototype.network;

import android.os.AsyncTask;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationAPI extends AsyncTask<Void, Void, String> {
    private OnLocateCompleteListener listener = null;

    @Override
    protected String doInBackground(Void... voids) {
        String location = "";
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://spott.p.rapidapi.com/places/ip/me")
                    .get()
                    .addHeader("X-RapidAPI-Key", "130066a719msh9dccf59b8d2fb55p14aaffjsncc24348ffbe3")
                    .addHeader("X-RapidAPI-Host", "spott.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                JSONObject json = new JSONObject(response.body().string());
                location = json.getString("name") + ", " + json.getJSONObject("country").getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    protected void onPostExecute(String text) {
        listener.onCompleted(text);
    }
    public interface OnLocateCompleteListener{
        void onCompleted(String text);
        void onError(Exception e);
    }
    public void setListener(OnLocateCompleteListener listener){
        this.listener=listener;
    }
}
