package com.example.steamprototype.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private List<Game> library;
    private List<Game> wishlist;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.library = new ArrayList<>();
        this.wishlist = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Game> getLibrary() {
        return library;
    }

    public void addToLibrary(Game game) {
        this.library.add(game);
    }

    public void setLibrary(List<Game> newLibrary) {
        this.library = newLibrary;
    }

    public List<Game> getWishlist() {
        return wishlist;
    }

    public void addToWishlist (Game game) {
        this.wishlist.add(game);
    }

    public void setWishlist(List<Game> wishlist) {
        this.wishlist = wishlist;
    }
}
