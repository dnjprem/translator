package com.google.mlkit.samples.nl.translate.java.model;

public class History {

    int id;
    String sourceCode;
    String sourceText;
    String targetCode;
    String targetText;
    String sourceDate;
    int isFavorite;

    public History(String sourceCode, String sourceText, String targetCode, String targetText, String sourceDate) {
        this.sourceCode = sourceCode;
        this.sourceText = sourceText;
        this.targetCode = targetCode;
        this.targetText = targetText;
        this.sourceDate = sourceDate;
    }

    public History(int id, String sourceCode, String sourceText, String targetCode, String targetText, String sourceDate, int isFavorite) {
        this.id = id;
        this.sourceCode = sourceCode;
        this.sourceText = sourceText;
        this.targetCode = targetCode;
        this.targetText = targetText;
        this.sourceDate = sourceDate;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }
}
