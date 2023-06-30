package com.voicetranslator.translate.languagetranslator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.listener.LanguageClickListener;
import com.voicetranslator.translate.languagetranslator.model.Language;


import java.util.HashMap;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {
    List<Language> languages;
    Context context;
    Language selectedLanguage;
    LanguageClickListener listener;
    HashMap<String, Task<Void>> pendingDownloads = new HashMap<>();
    RemoteModelManager modelManager;


    public LanguageAdapter(List<Language> languages, Context context, LanguageClickListener listener, Language selectedLanguage) {
        this.languages = languages;
        this.context = context;
        this.listener = listener;
        this.selectedLanguage = selectedLanguage;
        modelManager = RemoteModelManager.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        World.init(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_language, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Language item = languages.get(position);
        if (item.isDownloaded())
            holder.img_download.setVisibility(View.GONE);

        int flag = World.getFlagOf(item.getCode());
        int icon = getIcon(item.getCode());
        if (icon == -1) {
            holder.flagImageId.setImageResource(flag);
        } else {
            holder.flagImageId.setImageResource(icon);
        }

        holder.text_language_name.setText(item.getDisplayName());
        holder.itemView.setOnClickListener(view -> {
            if (item.isDownloaded()) {
                listener.onClick(item, holder.getAdapterPosition());
            } else {
                Print(context.getString(R.string.text_please_download_language_first));
            }
        });

        if (item.getCode().equalsIgnoreCase(selectedLanguage.getCode())) {
            holder.img_selected.setVisibility(View.VISIBLE);
        } else {
            holder.img_selected.setVisibility(View.GONE);
        }

        if (item.isDownloaded()) {
            holder.img_download.setVisibility(View.GONE);
        } else {
            if (item.isDownloading()) {
                holder.img_download.setVisibility(View.GONE);
                holder.loader.setVisibility(View.VISIBLE);
            } else {
                holder.loader.setVisibility(View.GONE);
                holder.img_download.setVisibility(View.VISIBLE);
            }
        }


        holder.img_download.setOnClickListener(view -> downloadLanguage(item));
    }

    public int getPositionByCode(String language) {
        for (int i = 0; i < languages.size(); i++) {
            if (language.equalsIgnoreCase(languages.get(i).getCode())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_language_name;
        ImageView flagImageId;
        ImageView img_selected;
        ImageView img_download;
        CircularProgressIndicator loader;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_language_name = itemView.findViewById(R.id.text_language_name);
            flagImageId = itemView.findViewById(R.id.img_flag);
            img_selected = itemView.findViewById(R.id.img_selected);
            img_download = itemView.findViewById(R.id.img_download);
            loader = itemView.findViewById(R.id.loader);
        }
    }

    public static int getIcon(String id) {
        String[] codes = {
                "sq", "zh", "cs", "da", "en", "eo",
                "ka", "el", "he", "hi", "ja", "ko",
                "fa", "sw", "ta", "te", "uk", "ur",
                "gu", "mr"
        };


        int[] resourceIds = {
                R.drawable.flag_albania, // sq -> Albanian
                R.drawable.flag_china, // zh -> Chinese
                R.drawable.flag_czech_republic, // cs -> czech
                R.drawable.flag_denmark, // da -> danish (Denmark)
                R.drawable.flag_united_kingdom, // en -> UnitedState
                R.drawable.flag_united_kingdom, // eo -> UnitedState
                R.drawable.flag_georgia, // ka -> Georgia
                R.drawable.flag_greece, // el -> Greek
                R.drawable.flag_israel, // he -> Hebrew (Israel)
                R.drawable.flag_india, // hi -> hindi (India)
                R.drawable.flag_japan, // ja -> Japan
                R.drawable.flag_north_korea, // ko -> South Korea
                R.drawable.flag_iran, // fa -> Farsi (Iran)
                R.drawable.flag_tanzania, // se -> Swahili
                R.drawable.flag_india, // ta -> Tamil (India)
                R.drawable.flag_india, //  te -> Telugu (India)
                R.drawable.flag_ukraine, // uk -> Ukraine
                R.drawable.flag_india, // ur -> Urdu
                R.drawable.flag_india, // gu -> Gujarati
                R.drawable.flag_india, // mr -> Marathi
        };

        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equalsIgnoreCase(id)) {
                return resourceIds[i];
            }
        }
        return -1;
    }


    private TranslateRemoteModel getModel(String languageCode) {
        return new TranslateRemoteModel.Builder(languageCode).build();
    }

    public void downloadLanguage(Language language) {
        TranslateRemoteModel model = getModel(TranslateLanguage.fromLanguageTag(language.getCode()));
        Task<Void> downloadTask;
//        Print(context.getString(R.string.text_downloading_start));
        if (pendingDownloads.containsKey(language.getCode())) {
            downloadTask = pendingDownloads.get(language.getCode());

            // found existing task. exiting
            if (downloadTask != null && !downloadTask.isCanceled()) {
//                Print(context.getString(R.string.text_already_downloaded));
                return;
            }
        }
        downloadTask =
                modelManager
                        .download(model, new DownloadConditions.Builder().build())
                        .addOnCompleteListener(
                                task -> {
                                    pendingDownloads.remove(language.getCode());
                                    int position = getPositionByCode(language.getCode());
                                    if (position != -1) {
                                        languages.get(position).setDownloaded(true);
                                        languages.get(position).setDownloading(false);
                                        notifyItemChanged(position);
                                    }
//                                    Print(context.getString(R.string.text_download_successfully));
                                    Log.d("==============>", "Language Downloaded");

                                });

        int position = getPositionByCode(language.getCode());
        if (position != -1) {
            languages.get(position).setDownloaded(false);
            languages.get(position).setDownloading(true);
            notifyItemChanged(position);
        }

        pendingDownloads.put(language.getCode(), downloadTask);

    }


    private void Print(String value) {
        Toast.makeText(context, value, Toast.LENGTH_LONG).show();
    }


}
