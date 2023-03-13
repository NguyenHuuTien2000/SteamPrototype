package com.example.steamprototype.entity;

public class LocalizedGame {
    private int id;
    private String translatedDesc;
    private String convertedPrice;

    public LocalizedGame(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTranslatedDesc() {
        return translatedDesc;
    }

    public void setTranslatedDesc(String translatedDesc) {
        this.translatedDesc = translatedDesc;
    }

    public String getConvertedPrice() {
        return convertedPrice;
    }

    public void setConvertedPrice(String convertedPrice) {
        this.convertedPrice = convertedPrice;
    }
}
