package com.example.steamprototype.network;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class TranslateAPI extends AsyncTask<String, String, String> {
    private OnTranslationCompleteListener listener;
    @Override
    protected String doInBackground(String... strings) {
        String[] strArr = (String[]) strings;
        String str = "";
        try {
            String encode = URLEncoder.encode(strArr[0], "utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=");
            sb.append(strArr[1]);
            sb.append("&tl=");
            sb.append(strArr[2]);
            sb.append("&dt=t&q=");
            sb.append(encode);
            URL convertEndpoint = new URL(sb.toString());
            HttpsURLConnection connection = (HttpsURLConnection) convertEndpoint.openConnection();
            if (connection.getResponseCode() == 200) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                connection.getOutputStream().write(byteArrayOutputStream.toByteArray());
                String byteArrayOutputStream2 = byteArrayOutputStream.toString();
                byteArrayOutputStream.close();
                JSONArray jSONArray = new JSONArray(byteArrayOutputStream2).getJSONArray(0);
                String str2 = str;
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONArray jSONArray2 = jSONArray.getJSONArray(i);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(jSONArray2.get(0).toString());
                    str2 = sb2.toString();
                }
                return str2;
            }
            connection.disconnect();
        } catch (Exception e) {
            listener.onError(e);
            return str;
        }
        return str;
    }
    @Override
    protected void onPostExecute(String text) {
        listener.onCompleted(text);
    }
    public interface OnTranslationCompleteListener{
        void onCompleted(String text);
        void onError(Exception e);
    }
    public void setOnTranslationCompleteListener(OnTranslationCompleteListener listener){
        this.listener=listener;
    }
}
