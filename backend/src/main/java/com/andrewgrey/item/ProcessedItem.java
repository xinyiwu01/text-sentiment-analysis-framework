package com.andrewgrey.item;

public class ProcessedItem {

    private final String text;
    private final String date;
    private final String time;
    private final float score;
    private final float magnitude;

    public ProcessedItem(String text, String date, String time, float score, float magnitude) {
        this.text = text;
        this.date = date;
        this.time = time;
        this.score = score;
        this.magnitude = magnitude;
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

    public float getScore() {
        return this.score;
    }

    public float getMagnitude() {
        return this.magnitude;
    }
}
