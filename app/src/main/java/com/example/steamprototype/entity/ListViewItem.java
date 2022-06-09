package com.example.steamprototype.entity;

public class ListViewItem {
    private String itemText;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ListViewItem(String itemText, int index) {
        this.itemText = itemText;
        this.index = index;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

}
