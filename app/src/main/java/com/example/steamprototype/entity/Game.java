package com.example.steamprototype.entity;

import java.io.Serializable;
import java.util.Date;

public class Game implements Serializable {
    private String gameID;
    private String title;
    private String publisher;
    private String developer;
    private String genre;
    private String platform;


    private Date releaseDate;
    private double price;

    public Game(String gameID, String title, String publisher, String developer, String genre, String platform, Date releaseDate, double price) {
        this.gameID = gameID;
        this.title = title;
        this.publisher = publisher;
        this.developer = developer;
        this.genre = genre;
        this.platform = platform;
        this.releaseDate = releaseDate;
        this.price = price;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
