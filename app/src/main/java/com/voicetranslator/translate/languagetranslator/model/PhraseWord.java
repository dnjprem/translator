package com.voicetranslator.translate.languagetranslator.model;

public class PhraseWord {
    String sourceText;
    String translatedSourceText;
    boolean isExpanded;
    String targetText;
    boolean isTranslating;
    boolean isError;
    boolean isTranslated;


    public PhraseWord(String sourceText, boolean isExpanded, String targetText) {
        this.sourceText = sourceText;
        this.translatedSourceText = "";
        this.isExpanded = isExpanded;
        this.targetText = targetText;
        isTranslating = false;
        isError = false;
        isTranslated = false;
    }

    public String getTranslatedSourceText() {
        return translatedSourceText;
    }

    public void setTranslatedSourceText(String translatedSourceText) {
        this.translatedSourceText = translatedSourceText;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public boolean isTranslating() {
        return isTranslating;
    }

    public void setTranslating(boolean translating) {
        isTranslating = translating;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isTranslated() {
        return isTranslated;
    }

    public void setTranslated(boolean translated) {
        isTranslated = translated;
    }
}
