package com.example.steamprototype.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game implements Serializable {
    private int gameID;
    private String title;
    private String publisher;
    private String developer;
    private String genre;
    private String platform;

    private Date releaseDate;
    private Date addedDate;
    private double price;
    private double discount;
    private String image;

    private double totalRate;

    private long rateCount;
    private String description;

    private boolean isRated;

    public Game(int gameID, String title, String publisher, String developer, String genre, String platform, Date releaseDate, double price, double discount, String image, double totalRate, long rateCount) {
        this.gameID = gameID;
        this.title = title;
        this.publisher = publisher;
        this.developer = developer;
        this.genre = genre;
        this.platform = platform;
        this.releaseDate = releaseDate;
        this.price = price;
        this.discount = discount;
        this.image = image;

        this.isRated = false;
        this.totalRate = totalRate;
        this.rateCount = rateCount;
    }

    public double getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(double totalRate) {
        this.totalRate = totalRate;
    }

    public void setRateCount(long rateCount) {
        this.rateCount = rateCount;
    }

    public void addRating(double rate) {
        this.totalRate += rate;
        this.rateCount += 1;
    }

    public double getRatingDouble() {
        if (totalRate == 0) {
            return totalRate;
        }
        return totalRate / rateCount;
    }

    public String getRatingString() {
        if (totalRate == 0) {
            return totalRate + "";
        }
        return String.format( "%.1f", totalRate / rateCount);
    }

    public long getRatingCount() {
        return rateCount;
    }

    public boolean isRated() {
        return isRated;
    }
    public void setRated() {
        this.isRated = true;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
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

    public String getReleaseDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.releaseDate);
    }

    public String getReleaseDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(this.releaseDate);
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

    public String getPriceString() {
        return this.price == 0.0 ? "Free" : "$" + String.format("%.2f", this.price);
    }

    public String getDiscountString() {
        return discount == 0.0 ? "" : "-" + discount + "%";
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public String getDateAddedString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.addedDate);
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
