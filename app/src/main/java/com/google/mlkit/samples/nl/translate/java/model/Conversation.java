package com.google.mlkit.samples.nl.translate.java.model;

import java.util.Date;

public class Conversation {
    int id;
    String sourceCode;
    String sourceText;
    String targetCode;
    String targetText;
    String sourceDate;
    String targetDate;
    int isSource;  // 0 -> False, 1 -> True

    public Conversation() {

    }

    public Conversation(String sourceCode, String sourceText, String targetCode, String targetText, String sourceDate, String targetDate, int isSource) {
        this.sourceCode = sourceCode;
        this.sourceText = sourceText;
        this.targetCode = targetCode;
        this.targetText = targetText;
        this.sourceDate = sourceDate;
        this.targetDate = targetDate;
        this.isSource = isSource;
    }

    public Conversation(String sourceCode, String sourceText, String targetCode, String targetText, int isSource) {
        this.sourceCode = sourceCode;
        this.sourceText = sourceText;
        this.targetCode = targetCode;
        this.targetText = targetText;
        this.isSource = isSource;
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

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public int getIsSource() {
        return isSource;
    }

    public void setIsSource(int isSource) {
        this.isSource = isSource;
    }
}
