package com.google.mlkit.samples.nl.translate.java.model;

public class OnBoarding {

    int image;
    String title;
    String firstSubTitle;
    String secondSubTitle;

    public OnBoarding(int image, String title, String firstSubTitle, String secondSubTitle) {
        this.image = image;
        this.title = title;
        this.firstSubTitle = firstSubTitle;
        this.secondSubTitle = secondSubTitle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstSubTitle() {
        return firstSubTitle;
    }

    public void setFirstSubTitle(String firstSubTitle) {
        this.firstSubTitle = firstSubTitle;
    }

    public String getSecondSubTitle() {
        return secondSubTitle;
    }

    public void setSecondSubTitle(String secondSubTitle) {
        this.secondSubTitle = secondSubTitle;
    }
}
