package com.example.android.weather;

public class SubDetailCardClass {

    private String title;
    private String value;
    private int icon;

    public SubDetailCardClass(String title,String value,int icon){
        this.title = title;
        this.value = value;
        this.icon = icon;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
