package com.voicetranslator.translate.languagetranslator.model;

public class Country {
    String countryCode;
    String phoneCode;
    String countryName;
    int countryFlag;
    boolean isSelected;

    public Country(String countryCode, String phoneCode, String countryName, int countryFlag) {
        this.countryCode = countryCode;
        this.phoneCode = phoneCode;
        this.countryName = countryName;
        this.countryFlag = countryFlag;
        this.isSelected = false;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(int countryFlag) {
        this.countryFlag = countryFlag;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
