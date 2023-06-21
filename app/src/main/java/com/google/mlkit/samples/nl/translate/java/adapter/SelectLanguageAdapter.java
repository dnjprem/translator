package com.google.mlkit.samples.nl.translate.java.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.listener.SelectLanguageClickListener;
import com.google.mlkit.samples.nl.translate.java.model.SelectLanguage;
import com.preference.PowerPreference;
import com.preference.Preference;
import java.util.List;

public class SelectLanguageAdapter extends RecyclerView.Adapter<SelectLanguageAdapter.MyViewHolder> {
    List<SelectLanguage> languageList;
    Preference preference;
    SelectLanguageClickListener listener;

    public SelectLanguageAdapter(List<SelectLanguage> languageList, SelectLanguageClickListener listener) {
        this.languageList = languageList;
        this.listener = listener;
        preference = PowerPreference.getFileByName("preferenceName");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_select_language, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SelectLanguage item = languageList.get(position);

        holder.img_flag.setImageResource(item.getResource());
        holder.txt_language_name.setText(item.getDisplayName());
        if (!item.isSelected()) {
            holder.img_selected.setImageResource(R.drawable.ic_uncheck);
        } else {
            holder.img_selected.setImageResource(R.drawable.ic_check_fill);
        }
        holder.itemView.setOnClickListener(view -> {
            selectionClear();
            languageList.get(position).setSelected(true);
            listener.onClick(item, holder.getAdapterPosition());
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void selectionClear() {
        for (SelectLanguage ele : languageList) {
            ele.setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_language_name;
        ImageView img_flag, img_selected;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_flag = itemView.findViewById(R.id.img_flag);
            txt_language_name = itemView.findViewById(R.id.txt_language_name);
            img_selected = itemView.findViewById(R.id.img_selected);
        }
    }
}
