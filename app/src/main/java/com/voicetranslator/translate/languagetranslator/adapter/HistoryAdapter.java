package com.voicetranslator.translate.languagetranslator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.listener.FavoriteClickListener;
import com.voicetranslator.translate.languagetranslator.model.History;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Object> histories;
    FavoriteClickListener listener;

    private static final int TYPE_ITEM_DATE_CONTAINER = 0; // for DateTime View
    private static final int TYPE_ITEM_HISTORY = 1; // for Conversation View

    public HistoryAdapter(ArrayList<History> histories,  FavoriteClickListener listener) {
        Collections.reverse(histories);
        this.histories = GeneralPreference.sortHistories(histories);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == TYPE_ITEM_DATE_CONTAINER) {
            view = layoutInflater.inflate(R.layout.item_view_conversation_time, parent, false);
            return new DateContainerViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.item_view_history, parent, false); // Add History Item....
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (histories.get(position) instanceof History) {
            History item = (History) histories.get(position);
            MyViewHolder holder = (MyViewHolder) viewHolder;

            if (item.getIsFavorite() == 0) {
                holder.img_favorite.setImageResource(R.drawable.ic_unlike);
            } else if (item.getIsFavorite() == 1) {
                holder.img_favorite.setImageResource(R.drawable.ic_like);
            }

            holder.img_favorite.setOnClickListener(view -> {
                item.setIsFavorite(setFavorite(item.getIsFavorite()));
                listener.onClick(histories, item.getId(), item.getIsFavorite(), position);
            });

            if (holder.getItemViewType() == TYPE_ITEM_HISTORY) {
                holder.text_source_language.setText(new Locale(item.getSourceCode()).getDisplayName());
                holder.text_source_text.setText(item.getSourceText());
                holder.text_target_language.setText(new Locale(item.getTargetCode()).getDisplayName());
                holder.text_target_text.setText(item.getTargetText());
            }

        } else if (histories.get(position) instanceof String) {
            DateContainerViewHolder holder = (DateContainerViewHolder) viewHolder;
            String item = (String) histories.get(position);
            holder.text_date_time.setText(item);
        }

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (histories.get(position) instanceof History) {
            return TYPE_ITEM_HISTORY;
        } else {
            return TYPE_ITEM_DATE_CONTAINER;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_source_language;
        TextView text_source_text;
        TextView text_target_language;
        TextView text_target_text;
        ImageView img_favorite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_source_language = itemView.findViewById(R.id.text_source_language);
            text_source_text = itemView.findViewById(R.id.text_source_text);
            text_target_language = itemView.findViewById(R.id.text_target_language);
            text_target_text = itemView.findViewById(R.id.text_target_text);
            img_favorite = itemView.findViewById(R.id.img_favorite);
        }
    }


    public static class DateContainerViewHolder extends RecyclerView.ViewHolder {
        TextView text_date_time;

        public DateContainerViewHolder(@NonNull View itemView) {
            super(itemView);
            text_date_time = itemView.findViewById(R.id.text_date_time);
        }
    }

    private int setFavorite(int fav) {
        if (fav == 0) return 1;
        if (fav == 1) return 0;
        return fav;
    }

}
