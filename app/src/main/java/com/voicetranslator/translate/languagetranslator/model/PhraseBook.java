package com.voicetranslator.translate.languagetranslator.model;

import java.util.ArrayList;

public class PhraseBook {
    private ArrayList<Category> categories;
    private String sourceLanguageCode;
    private String targetLanguageCode;

    public PhraseBook(ArrayList<Category> categories, String sourceLanguageCode, String targetLanguageCode) {
        this.categories = categories;
        this.sourceLanguageCode = sourceLanguageCode;
        this.targetLanguageCode = targetLanguageCode;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public String getTargetLanguageCode() {
        return targetLanguageCode;
    }

    public void setTargetLanguageCode(String targetLanguageCode) {
        this.targetLanguageCode = targetLanguageCode;
    }
}
