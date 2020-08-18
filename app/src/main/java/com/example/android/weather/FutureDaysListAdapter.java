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

public class FutureDaysListAdapter extends ArrayAdapter<WeatherDataClass> {

    private Context mContext;
    private List<WeatherDataClass> futureWeatherList;

    public FutureDaysListAdapter(Context context, ArrayList<WeatherDataClass> list) {
        super(context, 0 , list);
        mContext = context;
        futureWeatherList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.future_days_list_item,parent,false);

        WeatherDataClass weather = futureWeatherList.get(position);


        TextView dayName = (TextView) listItem.findViewById(R.id.future_day_name);
        dayName.setText(weather.getDay());

        TextView summary = (TextView) listItem.findViewById(R.id.future_summary);
        summary.setText(weather.getDescription());

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
        highLow.setText(weather.getHighTemperature(tempUnit)+ " / " + weather.getLowTemperature(tempUnit));

        return listItem;

    }
}
