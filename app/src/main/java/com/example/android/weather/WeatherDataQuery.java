package com.example.android.weather;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class WeatherDataQuery {

    public static final String LOG_TAG = "HTTPRequest";

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<WeatherDataClass> extractFeatureFromJson(String weatherJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(weatherJSON)) {
            return null;
        }
        ArrayList<WeatherDataClass> weather1  = new ArrayList<>();

        WeatherDataClass weatherData = null;
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            weatherData = new WeatherDataClass();

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(weatherJSON);

            JSONObject currentWeather = baseJsonResponse.getJSONObject("currently");

            JSONObject hourly = baseJsonResponse.getJSONObject("hourly");
            weatherData.setHourlySummary(hourly.getString("summary"));

            JSONArray hourlyData = hourly.getJSONArray("data");

            ArrayList<Long> timeArray = new ArrayList<>();
            ArrayList<Integer> tempArray = new ArrayList<>();
            ArrayList<String> iconArray = new ArrayList<>();
            ArrayList<Integer> precipProb = new ArrayList<>();
            for(int i = 1; i<=24 ; i=i+1){

                JSONObject currentHour = hourlyData.getJSONObject(i);

                timeArray.add(currentHour.getLong("time"));
                tempArray.add(currentHour.getInt("temperature"));
                iconArray.add(currentHour.getString("icon"));
                precipProb.add((int)(currentHour.getDouble("precipProbability")*100));
            }
            weatherData.setHourlyTimeArray(timeArray);
            weatherData.setHourlyTempArray(tempArray);
            weatherData.setHourlyIconArray(iconArray);
            weatherData.setHourlyPrecipitationArray(precipProb);

            DecimalFormat df = new DecimalFormat("#.00");

            String timeZone = baseJsonResponse.getString("timezone");
            weatherData.setTimeZone(timeZone);

            double latitude = baseJsonResponse.getDouble("latitude");
            latitude = Double.valueOf(df.format(latitude));
            weatherData.setLatitude(latitude);

            double longitude = baseJsonResponse.getDouble("longitude");
            longitude = Double.valueOf(df.format(longitude));
            weatherData.setLongitude(longitude);

            JSONObject daily = baseJsonResponse.getJSONObject("daily");

            JSONArray dailyData = daily.getJSONArray("data");

            JSONObject dailyDataToday = dailyData.getJSONObject(0);

            String weekSummary = daily.getString("summary");
            weatherData.setWeekSummary(weekSummary);

            int temperature = currentWeather.getInt("temperature");
            weatherData.setCurrentTemperture(temperature);

            int apparentTemperature = currentWeather.getInt("apparentTemperature");
            weatherData.setApparentTemperature(apparentTemperature);

            String summary = currentWeather.getString("summary");
            weatherData.setSummary(summary);

            String icon = currentWeather.getString("icon");
            weatherData.setIcon(icon);

            long time = currentWeather.getLong("time");
            weatherData.setTime(time);

            int uvIndex = currentWeather.getInt("uvIndex");
            weatherData.setUVindex(uvIndex);

            double precipitation = currentWeather.getDouble("precipProbability");
            weatherData.setPrecipitation((int)(precipitation*100));

            double windSpeed = currentWeather.getDouble("windSpeed");
            weatherData.setWindSpeed((int)(windSpeed*1.61));

            double windDirection = currentWeather.getDouble("windBearing");
            weatherData.setWindDirection(windDirection);

            double cloudiness = currentWeather.getDouble("cloudCover");
            weatherData.setCloudiness((int)(cloudiness*100));

            int visibiiity = currentWeather.getInt("visibility");
            weatherData.setVisibility(visibiiity);

            double humidity = currentWeather.getDouble("humidity");
            weatherData.setHumidity((int)(humidity*100));

            double pressure = currentWeather.getDouble("pressure");
            weatherData.setPressure((int)pressure);

            int highTemperature = dailyDataToday.getInt("temperatureHigh");
            weatherData.setHighTemperature(highTemperature);

            long temperatureHighTime = dailyDataToday.getLong("temperatureHighTime");
            weatherData.setHighTemperatureTime(temperatureHighTime);

            int lowTemperature = dailyDataToday.getInt("temperatureLow");
            weatherData.setLowTemperature(lowTemperature);

            long temperatureLowTime = dailyDataToday.getLong("temperatureLowTime");
            weatherData.setLowTemperatureTime(temperatureLowTime);

            long sunriseTime = dailyDataToday.getLong("sunriseTime");
            weatherData.setSunRiseTime(sunriseTime);

            long sunsetTime = dailyDataToday.getLong("sunsetTime");
            weatherData.setSunSetTime(sunsetTime);

            if(dailyDataToday.has("precipType")){
                String precipitationType = dailyDataToday.getString("precipType");
                weatherData.setPrecipitationType(precipitationType);
            }


            weather1.add(weatherData);

            for(int i=1; i<8; i++){

                WeatherDataClass w = new WeatherDataClass();
                JSONObject futureData = dailyData.getJSONObject(i);

                int futureHighTemperature = futureData.getInt("temperatureHigh");
                w.setHighTemperature(futureHighTemperature);

                String futureSummary = futureData.getString("summary");
                w.setSummary(futureSummary);

//                String futureDescription = futureData.getString("icon");
//                w.setDescription(futureDescription);

                String futureIcon = futureData.getString("icon");
                w.setIcon(futureIcon);

                long futureTime = futureData.getLong("time");
                w.setTime(futureTime);

                int futureLowTemperature = futureData.getInt("temperatureLow");
                w.setLowTemperature(futureLowTemperature);

                double futurePrecipitation = futureData.getDouble("precipProbability");
                w.setPrecipitation((int)(futurePrecipitation*100));

                if(futureData.has("precipType")){
                    String futurePrecipitationType = futureData.getString("precipType");
                    w.setPrecipitationType(futurePrecipitationType);
                }

                long futureTemperatureHighTime = futureData.getLong("temperatureHighTime");
                w.setHighTemperatureTime(futureTemperatureHighTime);

                long futureTemperatureLowTime = futureData.getLong("temperatureLowTime");
                w.setLowTemperatureTime(futureTemperatureLowTime);

                double futureHumidity = futureData.getDouble("humidity");
                w.setHumidity((int)(futureHumidity*100));

                double futurePressure = futureData.getDouble("pressure");
                w.setPressure((int)futurePressure);

                int futureUVIndex = futureData.getInt("uvIndex");
                w.setUVindex(futureUVIndex);

                double futureWindSpeed = futureData.getDouble("windSpeed");
                w.setWindSpeed((int)(futureWindSpeed*1.61));

                double futureWindDirection = futureData.getDouble("windBearing");
                w.setWindDirection(futureWindDirection);

                double futureCloudiness = futureData.getDouble("cloudCover");
                w.setCloudiness((int)(futureCloudiness*100));

                String futureTimeZone = baseJsonResponse.getString("timezone");
                w.setTimeZone(futureTimeZone);

                weather1.add(w);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("WeatherDataQuery", "Problem parsing the weather JSON results", e);
        }

        return weather1;
    }

    public static ArrayList<WeatherDataClass> fetchWeatherData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link WeatherDataClass}s
        ArrayList<WeatherDataClass> weather = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link WeatherDataClass}s
        return weather;
    }

}
