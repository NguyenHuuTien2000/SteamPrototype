package com.example.steamprototype.data_op;

import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.LocalizedGame;
import com.example.steamprototype.network.ConversionAPI;
import com.example.steamprototype.network.TranslateAPI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LocalizeGameAttribute {
    private Map<Integer, LocalizedGame> localizedENGamesMap;
    private Map<Integer,LocalizedGame> localizedCNGamesMap;
    private Map<Integer,LocalizedGame> localizedFRGamesMap;
    private Map<Integer,LocalizedGame> localizedVIGamesMap;
    private Map<Integer,LocalizedGame> localizedJPGamesMap;
    private Map<Integer,LocalizedGame> localizedSPGamesMap;

    private TranslateAPI translateAPI;

    private ConversionAPI conversionAPI;

    private NumberFormat numberFormat = NumberFormat.getInstance();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private double exchangeRateVND = 23_650;
    private double exchangeRateCNY = 6.9025;
    private double exchangeRateJPY = 134.54;
    private double exchangeRateEUR = 0.9366;

    public LocalizeGameAttribute(ArrayList<Game> games) {
        numberFormat.setMaximumFractionDigits(0);

        this.localizedCNGamesMap = new HashMap<>();
        this.localizedFRGamesMap = new HashMap<>();
        this.localizedVIGamesMap = new HashMap<>();
        this.localizedJPGamesMap = new HashMap<>();
        this.localizedSPGamesMap = new HashMap<>();
        this.localizedENGamesMap = new HashMap<>();

        for (Game game : games) {
            int id = game.getGameID();
            localizedCNGamesMap.put(id,localize("zh", game));
            localizedFRGamesMap.put(id,localize("fr", game));
            localizedVIGamesMap.put(id,localize("vi", game));
            localizedJPGamesMap.put(id,localize("ja", game));
            localizedSPGamesMap.put(id,localize("es", game));
            localizedENGamesMap.put(id,localize("en", game));
        }
    }

    public LocalizedGame getLocalizedGame (String countryCode, int id) {
        switch (countryCode) {
            case "vi":
                return localizedVIGamesMap.get(id);
            case "zh":
                return localizedCNGamesMap.get(id);
            case "ja":
                return localizedJPGamesMap.get(id);
            case "fr":
                return localizedFRGamesMap.get(id);
            case "es":
                return localizedSPGamesMap.get(id);
            default:
                return localizedENGamesMap.get(id);
        }
    }

    private LocalizedGame localize(String countryCode, Game game) {

        this.translateAPI = new TranslateAPI();
        this.conversionAPI = new ConversionAPI();

        int id = game.getGameID();
        LocalizedGame localizedGame;
        localizedGame = new LocalizedGame(id);

        String desc = game.getDescription();
        //localizedGame.setTranslatedDesc(getTranslation(desc, countryCode));
        localizedGame.setTranslatedDesc(desc);

        double price = game.getPrice();
        localizedGame.setConvertedPrice(getConvertedPrice(countryCode, price));

        return localizedGame;
    }

    private String getTranslation(String strings, String countryCode) {
        final String[] translatedStr = {""};
        translateAPI.setOnTranslationCompleteListener(new TranslateAPI.OnTranslationCompleteListener() {
            @Override
            public void onCompleted(String text) {
                if (text.equals("")) {
                    text = strings;
                }
                translatedStr[0] = text;
            }
            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }
        });
        translateAPI.execute(strings ,"en", countryCode);
        return translatedStr[0];
    }
    private String getConvertedPrice(String countryCode, double price) {
        if (price == 0) {
            return "Free";
        }
        String currencyCode;
        double convertedPrice = price;

        switch (countryCode) {
            case "vi":
                currencyCode = "VND";
                convertedPrice *= exchangeRateVND;
                return numberFormat.format(convertedPrice) + " " + currencyCode;
            case "zh":
                convertedPrice *= exchangeRateCNY;
                currencyFormat.setCurrency(Currency.getInstance(Locale.CHINA));
                break;
            case "ja":
                convertedPrice *= exchangeRateJPY;
                currencyFormat.setCurrency(Currency.getInstance(Locale.JAPAN));
                break;
            case "fr":
            case "es":
                currencyCode = "EUR";
                convertedPrice *= exchangeRateEUR;
                return numberFormat.format(convertedPrice) + " " + currencyCode;
            default:
                currencyFormat.setCurrency(Currency.getInstance(Locale.US));
        }
        return currencyFormat.format(convertedPrice);
    }

//    private String getConvertedPrice(String currencyCode, String price) {
//        final String[] convertedStr = {""};
//        conversionAPI.setListener(new ConversionAPI.OnConversionCompleteListener() {
//            @Override
//            public void onCompleted(String text) {
//                if (text.equals("")) {
//                    text = price;
//                }
//                String formattedText = numberFormat.format(text);
////                if (formattedText.length() > 5 && formattedText.contains(".")) {
////                    formattedText = formattedText.substring(0,formattedText.indexOf("."));
////                }
//                convertedStr[0] = formattedText + " " + currencyCode;
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//        conversionAPI.execute(currencyCode, "" + price);
//        return convertedStr[0];
//    }
}