package com.example.android.weather;

public class HourlyDetailsCardClass {

    private String time;
    private String temp;
    private int icon;
    private int precip;

    public HourlyDetailsCardClass(String title, String value, int icon, int precip) {
        this.time = title;
        this.temp = value;
        this.icon = icon;
        this.precip = precip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPrecip() {
        return precip+"%";
    }

    public void setPrecip(int precip) {
        this.precip = precip;
    }
}
