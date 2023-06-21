package com.google.mlkit.samples.nl.translate.java.model;

import java.util.ArrayList;

public class Category {
    String categoryName;
    private ArrayList<PhraseWord> words;

    public Category(String categoryName, ArrayList<PhraseWord> words) {
        this.categoryName = categoryName;
        this.words = words;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<PhraseWord> getWords() {
        return words;
    }

    public void setWords(ArrayList<PhraseWord> words) {
        this.words = words;
    }

}
