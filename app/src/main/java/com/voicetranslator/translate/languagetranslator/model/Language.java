package com.voicetranslator.translate.languagetranslator.model;

import java.util.Locale;

public class Language {
    String displayName;
    String code;
    int resource;
    boolean isDownloaded;
    boolean isDownloading;

    public Language(String displayName, String code, int resource, boolean isDownloaded, boolean isDownloading) {
        this.displayName = displayName;
        this.code = code;
        this.resource = resource;
        this.isDownloaded = isDownloaded;
        this.isDownloading = isDownloading;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public String getDisplayName() {
        return new Locale(code).getDisplayName();
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
}
