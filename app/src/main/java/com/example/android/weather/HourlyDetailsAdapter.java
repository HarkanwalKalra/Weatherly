package com.example.android.weather;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HourlyDetailsAdapter extends RecyclerView.Adapter<HourlyDetailsAdapter.MyViewHolder> {

    private ArrayList<HourlyDetailsCardClass> cardSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView precip;
        TextView temp;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.time = itemView.findViewById(R.id.hourly_time);
            this.temp = itemView.findViewById(R.id.hourly_temp);
            this.icon = itemView.findViewById(R.id.hourly_icon);
            this.precip = itemView.findViewById(R.id.hourly_precipitation);
        }
    }

    public HourlyDetailsAdapter(ArrayList<HourlyDetailsCardClass> card) {
        this.cardSet = card;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_details_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewTime = holder.time;
        TextView textViewTemp = holder.temp;
        TextView textViewPrecip = holder.precip;
        ImageView imageViewIcon = holder.icon;

        textViewTime.setText(cardSet.get(listPosition).getTime());
        textViewTemp.setText(cardSet.get(listPosition).getTemp());
        textViewPrecip.setText(cardSet.get(listPosition).getPrecip());
        imageViewIcon.setImageResource(cardSet.get(listPosition).getIcon());
    }

    @Override
    public int getItemCount() {
        return cardSet.size();
    }
}

