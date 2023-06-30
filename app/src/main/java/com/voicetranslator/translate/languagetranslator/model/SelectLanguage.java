package com.voicetranslator.translate.languagetranslator.model;

public class SelectLanguage {
    String code;
    String displayName;
    int resource;
    boolean isSelected;

    public SelectLanguage() {
    }

    public SelectLanguage(String code, String displayName, int resource, boolean isSelected) {
        this.code = code;
        this.displayName = displayName;
        this.resource = resource;
        this.isSelected = isSelected;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
