package com.voicetranslator.translate.languagetranslator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.listener.CountryClickListener;
import com.voicetranslator.translate.languagetranslator.model.Country;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    ArrayList<Country> mainCountries;

    ArrayList<Country> countries;
    CountryClickListener listener;

    public CountryAdapter(ArrayList<Country> mainCountries, CountryClickListener listener) {
        this.mainCountries = mainCountries;
        this.listener = listener;
        this.countries = new ArrayList<>();
        this.countries.addAll(mainCountries);
    }

    public void filterList(String filter) {

        if (filter == null || filter.trim().equals("")) {
            countries = new ArrayList<>();
            countries.addAll(mainCountries);
        } else {
            countries = new ArrayList<>();
            for (Country item : mainCountries) {
                if (item.getCountryName().toLowerCase().contains(filter.toLowerCase())) {
                    countries.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_view_country, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Country item = countries.get(position);

        if (!item.isSelected()) {
            holder.img_selected.setImageResource(R.drawable.ic_uncheck);
        } else {
            holder.img_selected.setImageResource(R.drawable.ic_check_fill);
        }
        holder.img_flag.setImageResource(item.getCountryFlag());
        holder.text_language_name.setText(item.getCountryName());
        holder.itemView.setOnClickListener(view ->
        {
            selectionClear();
            item.setSelected(true);
            listener.onClick(item, holder.getAdapterPosition());
        });


    }

    private void selectionClear() {
        for (Country item : countries) {
            item.setSelected(false);

        }
        for (Country item : mainCountries) {
            item.setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_flag;
        ImageView img_selected;
        TextView text_language_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_flag = itemView.findViewById(R.id.img_flag);
            img_selected = itemView.findViewById(R.id.img_selected);
            text_language_name = itemView.findViewById(R.id.text_language_name);
        }
    }
}
