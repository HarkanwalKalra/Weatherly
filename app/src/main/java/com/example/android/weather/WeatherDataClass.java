package com.example.android.weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherDataClass {
    private String hourlySummary;

    private ArrayList<Long> hourlyTimeArray;
    private ArrayList<Integer> hourlyTempArray;
    private ArrayList<String> hourlyIconArray;
    private ArrayList<Integer> hourlyPrecipitationArray;

    private double mLatitude;

    private double mLongitude;

    private String mTimeZone;

    private String mWeekSummary;

    private int mCurrentTemperature;

    private int mApparentTemperature;

    private int mHighTemperature;

    private long mHighTemperatureTime;

    private int mLowTemperature;

    private long mLowTemperatureTime;

    private String mCityName;

    private String mSummary;

    private String mDescription;

    private int mPrecipitation;

    private String mPrecipitationType = "rain";

    private int mWindSpeed;

    private int mHumidity;

    private long mTime;

    private double windDirection;

    private String mIcon;

    private int mCloudiness;

    private int mPressure;

    private int mVisibility;

    private long mSunRiseTime;

    private long mSunSetTime;

    private int mUVindex;

    public String getHourlySummary() {
        return hourlySummary;
    }

    public void setHourlySummary(String hourly) {
        this.hourlySummary = hourly;
    }

    public ArrayList<Long> getHourlyTimeArray() {
        return hourlyTimeArray;
    }

    public void setHourlyTimeArray(ArrayList<Long> hourly) {
        this.hourlyTimeArray = hourly;
    }

    public ArrayList<Integer> getHourlyTempArray() {
        return hourlyTempArray;
    }

    public void setHourlyTempArray(ArrayList<Integer> hourly) {
        this.hourlyTempArray = hourly;
    }

    public ArrayList<String> getHourlyIconArray() {
        return hourlyIconArray;
    }

    public void setHourlyIconArray(ArrayList<String> hourly) {
        this.hourlyIconArray = hourly;
    }

    public ArrayList<Integer> getHourlyPrecipitationArray() {
        return hourlyPrecipitationArray;
    }

    public void setHourlyPrecipitationArray(ArrayList<Integer> hourly) {
        this.hourlyPrecipitationArray = hourly;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getPrecipitationType() {
        return mPrecipitationType;
    }

    public void setPrecipitationType(String mPrecipitationType) {
        this.mPrecipitationType = mPrecipitationType;
    }

    public int getUVindex() {
        return mUVindex;
    }

    public void setUVindex(int mUVindex) {
        this.mUVindex = mUVindex;
    }

    public WeatherDataClass() {

    }

    public int getCurrentTemperture(String unit) {
        if (unit.equals("°F")) {
            return (mCurrentTemperature * 9 / 5) + 32;
        } else {
            return mCurrentTemperature;
        }
    }

    public void setCurrentTemperture(int mCurrentTemperature) {
        this.mCurrentTemperature = mCurrentTemperature;
    }

    public int getApparentTemperature(String unit) {
        if (unit.equals("°F")) {
            return (mApparentTemperature * 9 / 5) + 32;
        } else {
            return mApparentTemperature;
        }
    }

    public void setApparentTemperature(int mApparentTemperature) {
        this.mApparentTemperature = mApparentTemperature;
    }


    String getDescription() {
        String weather;
        switch (mIcon) {
            case "clear-day":
                weather = "Clear";
                break;
            case "clear-night":
                weather = "Clear";
                break;
            case "rain":
                weather = "Rain";
                break;
            case "snow":
                weather = "Snow";
                break;
            case "sleet":
                weather = "Sleet";
                break;
            case "wind":
                weather = "Wind";
                break;
            case "fog":
                weather = "Fog";
                break;
            case "cloudy":
                weather = "Cloudy";
                break;
            case "partly-cloudy-day":
                weather = "Partly Cloudy Day";
                break;
            case "partly-cloudy-night":
                weather = "Partly Cloudy Night";
                break;
            default:
                weather = "Clear";
                break;
        }
        return weather;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getPrecipitation() {
        return mPrecipitation;
    }

    public void setPrecipitation(int mPrecipitation) {
        this.mPrecipitation = mPrecipitation;
    }

    public int getWindSpeed(String unit) {
        if (unit.equals("km/hr")) {
            return (int) (mWindSpeed * 3.6);
        } else if (unit.equals("mi/hr")) {
            return (int) (mWindSpeed * 2.236);
        } else {
            return mWindSpeed;
        }
    }

    public void setWindSpeed(int mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public String getHighTemperatureTime() {
        return getLocalTime(mHighTemperatureTime);
    }

    public void setHighTemperatureTime(long mHighTemperatureTime) {
        this.mHighTemperatureTime = mHighTemperatureTime;
    }

    public String getLowTemperatureTime() {
        return getLocalTime(mLowTemperatureTime);
    }

    public void setLowTemperatureTime(long mLowTemperatureTime) {
        this.mLowTemperatureTime = mLowTemperatureTime;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int mHumidity) {
        this.mHumidity = mHumidity;
    }

    public void setTime(long mUpdatedTime) {
        this.mTime = mUpdatedTime;
    }

    public long getTime() {
        return mTime;
    }

    public int getHighTemperature(String unit) {
        if (unit.equals("°F")) {
            return (mHighTemperature * 9 / 5) + 32;
        } else {
            return mHighTemperature;
        }
    }

    public void setHighTemperature(int mHighTemperature) {
        this.mHighTemperature = mHighTemperature;
    }

    public int getLowTemperature(String unit) {
        if (unit.equals("°F")) {
            return (mLowTemperature * 9 / 5) + 32;
        } else {
            return mLowTemperature;
        }
    }

    public void setLowTemperature(int mLowTemperature) {
        this.mLowTemperature = mLowTemperature;
    }

    public String getDay() {
        String day;

        day = new SimpleDateFormat("EEE", Locale.getDefault()).format(mTime * 1000);

        return day;
    }

    private String getLocalTime(long t) {
        String time;

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        time = sdf.format(t * 1000);

        return time;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public int getIcon() {
        return getIconForWeather(mIcon);
    }

    public String getIconName() {
        return mIcon;
    }

    public int getIconForWeather(String iconDetail) {

        int icon;

        switch (iconDetail) {
            case "clear-day":
                icon = R.drawable.clear_day;
                break;
            case "clear-night":
                icon = R.drawable.clear_night;
                break;
            case "rain":
                icon = R.drawable.rain;
                break;
            case "snow":
                icon = R.drawable.snow;
                break;
            case "sleet":
                icon = R.drawable.sleet;
                break;
            case "wind":
                icon = R.drawable.wind;
                break;
            case "fog":
                icon = R.drawable.fog;
                break;
            case "cloudy":
                icon = R.drawable.overcast;
                break;
            case "partly-cloudy-day":
                icon = R.drawable.partly_cloudy_day;
                break;
            case "partly-cloudy-night":
                icon = R.drawable.partly_cloudy_night;
                break;
            default:
                icon = R.drawable.clear_weather_icon;
                break;
        }
        return icon;
    }

    public void setIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public int getCloudiness() {
        return mCloudiness;
    }

    public void setCloudiness(int mCloudiness) {
        this.mCloudiness = mCloudiness;
    }

    public int getPressure() {
        return mPressure;
    }

    public void setPressure(int mPressure) {
        this.mPressure = mPressure;
    }

    public int getVisibility(String unit) {

        if (unit.equals("mi/hr")) {
            return (int) (mVisibility / 1.61);
        } else {
            return mVisibility;
        }
    }

    public void setVisibility(int mVisibility) {
        this.mVisibility = mVisibility;
    }

    public String getSunRiseTime() {

        return getLocalTime(mSunRiseTime);
    }

    public void setSunRiseTime(long mSunRiseTime) {
        this.mSunRiseTime = mSunRiseTime;
    }

    public String getSunSetTime() {

        return getLocalTime(mSunSetTime);
    }

    public void setSunSetTime(long mSunSetTime) {
        this.mSunSetTime = mSunSetTime;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getWeekSummary() {
        return mWeekSummary;
    }

    public void setWeekSummary(String mWeekSummary) {
        this.mWeekSummary = mWeekSummary;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getTimeZone() {
        return mTimeZone;
    }
}
