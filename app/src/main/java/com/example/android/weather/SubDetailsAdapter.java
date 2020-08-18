package com.example.android.weather;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SubDetailsAdapter extends RecyclerView.Adapter<SubDetailsAdapter.MyViewHolder> {

    private ArrayList<SubDetailCardClass> cardSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView value;
        ImageView icon;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.value = itemView.findViewById(R.id.value);
            this.icon = itemView.findViewById(R.id.icon);
        }
    }

    public SubDetailsAdapter(ArrayList<SubDetailCardClass> card) {
        this.cardSet = card;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_details_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewTitle = holder.title;
        TextView textViewValue = holder.value;
        ImageView imageViewIcon = holder.icon;;

        textViewTitle.setText(cardSet.get(listPosition).getTitle());
        textViewValue.setText(cardSet.get(listPosition).getValue());
        imageViewIcon.setImageResource(cardSet.get(listPosition).getIcon());

    }

    @Override
    public int getItemCount() {
        return cardSet.size();
    }
}

