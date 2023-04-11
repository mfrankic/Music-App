package com.example.musicapp;

public class CarouselItem {
    private final int imageResId;
    private final String text;

    public CarouselItem(int imageResId, String text) {
        this.imageResId = imageResId;
        this.text = text;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }
}

