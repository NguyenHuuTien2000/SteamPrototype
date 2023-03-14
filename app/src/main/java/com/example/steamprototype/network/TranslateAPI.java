package com.example.steamprototype.network;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateAPI extends AsyncTask<String, String, String> {
    private OnTranslationCompleteListener listener = null;
    @Override
    protected String doInBackground(String... strings) {
        String translatedText = "";
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("text", strings[0])
                    .add("target", strings[1])
                    .add("source", "en")
                    .build();

            Request request = new Request.Builder()
                    .url("https://translate-plus.p.rapidapi.com/translate")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("X-RapidAPI-Key", "130066a719msh9dccf59b8d2fb55p14aaffjsncc24348ffbe3")
                    .addHeader("X-RapidAPI-Host", "translate-plus.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                JSONObject json = new JSONObject(response.body().string());
                translatedText = json.getJSONObject("data").getString("translatedText");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translatedText;
    }
    @Override
    protected void onPostExecute(String text) {
        listener.onCompleted(text);
    }
    public interface OnTranslationCompleteListener{
        void onCompleted(String text);
        void onError(Exception e);
    }
    public void setListener(OnTranslationCompleteListener listener){
        this.listener=listener;
    }
}
