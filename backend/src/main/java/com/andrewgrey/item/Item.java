package com.andrewgrey.item;


public class Item {

    private final String text;
    private final String date;
    private final String time;

    public Item(String text, String date, String time) {
        this.text = text;
        this.date = date;
        this.time = time;
    }

    public String getText() {
        return this.text;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }
}
