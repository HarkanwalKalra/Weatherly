package com.example.android.weather;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ForecastWeatherActivityListAdapter extends ArrayAdapter<WeatherDataClass> {

    private Context mContext;
    private List<WeatherDataClass> futureWeatherList = new ArrayList<>();

    public ForecastWeatherActivityListAdapter(Context context, ArrayList<WeatherDataClass> list) {
        super(context, 0 , list);
        mContext = context;
        futureWeatherList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.forecast_activity_list_item,parent,false);

        WeatherDataClass weather = futureWeatherList.get(position);

        TextView dayName = (TextView) listItem.findViewById(R.id.future_day_name);
        dayName.setText(weather.getDay());


        TextView desc = (TextView) listItem.findViewById(R.id.future_summary);
        desc.setText(weather.getDescription());

        ImageView icon = (ImageView)listItem.findViewById(R.id.future_icon);
        icon.setImageResource(weather.getIcon());

        PreferenceManager.setDefaultValues(mContext, R.xml.preferences, false);
        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(mContext);

        String tempUnit = sharedPref.getString("temp_units", "1");
        if (tempUnit.equals("2")) {
            tempUnit = "°F";
        } else {
            tempUnit = "°C";
        }

        TextView highLow = (TextView) listItem.findViewById(R.id.future_highLow);
        highLow.setText(weather.getHighTemperature(tempUnit) + " / " + weather.getLowTemperature(tempUnit));

        TextView summary = (TextView) listItem.findViewById(R.id.day_summary);
        summary.setText(weather.getSummary());

        TextView precipProb = (TextView) listItem.findViewById(R.id.precipProb_value);
        precipProb.setText(weather.getPrecipitation()+"%");

        int precipitation_icon = R.drawable.precipitation;
        if (weather.getPrecipitationType().equals("snow")) {
            precipitation_icon = R.drawable.snow_icon;
        }
        ImageView preipType = listItem.findViewById(R.id.precipType_icon);
        preipType.setImageResource(precipitation_icon);

        TextView highTempTime = (TextView) listItem.findViewById(R.id.highTempTime_value);
        highTempTime.setText(weather.getHighTemperatureTime());

        TextView lowTempTime = (TextView) listItem.findViewById(R.id.lowTempTime_value);
        lowTempTime.setText(weather.getLowTemperatureTime());

        TextView humidity = (TextView) listItem.findViewById(R.id.humidity_value);
        humidity.setText(weather.getHumidity()+"%");

        TextView pressure = (TextView) listItem.findViewById(R.id.pressure_value);
        pressure.setText(weather.getPressure()+"hPa");

        TextView uvIndex = (TextView) listItem.findViewById(R.id.uvIndex_value);
        uvIndex.setText(String.valueOf(weather.getUVindex()));

        return listItem;

    }
}
