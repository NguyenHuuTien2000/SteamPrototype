package com.example.steamprototype.network;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class ConversionAPI extends AsyncTask<String, String, String> {
    private OnConversionCompleteListener listener;

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder sb = new StringBuilder();
        String[] strArr = (String[]) strings;
        String str = "";
        try {
            sb.append("https://api.apilayer.com/fixer/convert?to=")
                    .append(strArr[0])
                    .append("&from=USD")
                    .append("&amount=")
                    .append(strArr[1]);
            URL convertEndpoint = new URL(sb.toString());
            HttpsURLConnection connection = (HttpsURLConnection) convertEndpoint.openConnection();
            connection.setRequestProperty("apikey", "GQJhy9mEXtchlY0ZNlA4t2fT9CE6TPrH");
            if (connection.getResponseCode() == 200) {
                InputStream responseBody = connection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("result")) {
                        str = jsonReader.nextString();
                        return str;
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.close();
                connection.disconnect();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return str;
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
